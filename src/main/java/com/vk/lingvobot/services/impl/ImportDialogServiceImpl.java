package com.vk.lingvobot.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.lingvobot.parser.importDialogParser.ImportDialogParser;
import com.vk.lingvobot.parser.importDialogParser.LinkData;
import com.vk.lingvobot.parser.importDialogParser.NodeData;
import com.vk.lingvobot.services.ImportDialogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ImportDialogServiceImpl implements ImportDialogService {

    private Gson gson = new GsonBuilder().create();

    public void importDialog() {
        System.out.println("Import............................");
        Path path = Paths.get("C:/Work/test.txt");
        byte[] bytes = new byte[0];

        try {
            bytes = Files.readAllBytes(path);
        } catch (IOException ioe) {
            log.error("IOException while reading file from disk. " + ioe.getStackTrace());
        }

        String string = null;

        try {
            string = new String(bytes,"Cp1251");
        } catch (UnsupportedEncodingException e) {
            log.error("File can not be encoded to cp1251. Too bad. " + e.getStackTrace());
        }

        ImportDialogParser importDialogData = gson.fromJson(string, ImportDialogParser.class);

        ImportDialogParser importDialogDataWithPositiveValues = convertNodesIntoPositiveValues(importDialogData);
        findAllConnections(importDialogDataWithPositiveValues); // <-- I forget why it's here
        detectAllRoundedRectanglesWithRelations(importDialogDataWithPositiveValues); //<--This is for future keyboard feature
        ImportDialogParser importDialogParser = squashAllKeyboardsCandidates(importDialogData); // <-- Delete keyboard's messages from whole pool

        mapImportParserIntoOurDatabaseStructure(importDialogParser);
    }


    private ImportDialogParser convertNodesIntoPositiveValues(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        nodeDataList.forEach(s -> s.setKey(Math.abs(s.getKey())));
        linkDataList.forEach(s -> s.setFrom(Math.abs(s.getFrom())));
        linkDataList.forEach(s -> s.setTo(Math.abs(s.getTo())));

        importDialogParser.setLinkDataList(linkDataList);
        importDialogParser.setNodeDataList(nodeDataList);


        return importDialogParser;
    }

    /**
     *  f.e. if (3,4,5) - rectangular objects then (1,3), (1,4), (1,5), (3,6), (4,6), (5,6) --> (1,6)
     * @param importDialogParser
     * @return
     */
    private ImportDialogParser squashAllKeyboardsCandidates(ImportDialogParser importDialogParser) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        ImportDialogParser importDialogDataWithPositiveValues = convertNodesIntoPositiveValues(importDialogParser);
        List<Map.Entry<Integer, List<Integer>>> entries = detectAllRoundedRectanglesWithRelations(importDialogDataWithPositiveValues);
        for (Map.Entry<Integer, List<Integer>> entry : entries) {
            List<Integer> value = entry.getValue();
            for (Integer it : value) {
                List<Integer> specificNodeConnectionsToAndFrom = findSpecificNodeConnectionsToAndFrom(importDialogParser, it);
                if (specificNodeConnectionsToAndFrom.size() == 2) {

                    LinkData newLink = new LinkData(specificNodeConnectionsToAndFrom.get(0), specificNodeConnectionsToAndFrom.get(1));
                    LinkData oldLinkOneSide = new LinkData(specificNodeConnectionsToAndFrom.get(0), it);
                    LinkData oldLinkOtherSide = new LinkData(it, specificNodeConnectionsToAndFrom.get(1));

                    boolean first = linkDataList.contains(oldLinkOneSide);
                    boolean second = linkDataList.contains(oldLinkOtherSide);

                    linkDataList.add(newLink);
                    linkDataList.remove(oldLinkOneSide);
                    linkDataList.remove(oldLinkOtherSide);
                }
            }
        }

        importDialogParser.setLinkDataList(linkDataList);
        return importDialogParser;

    }

    private void findAllConnections(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        for (NodeData nodeData : nodeDataList) {
            int key = nodeData.getKey();
            List<Integer> connections = new ArrayList<>();
            for (LinkData linkData : linkDataList) {
                int from = linkData.getFrom();
                if (key == from) {
                    int to = linkData.getTo();
                    connections.add(to);
                }
            }
        }
    }

    private List<Integer> findSpecificNodeConnectionsToAndFrom(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        List<Integer> connections = new ArrayList<>();

        for (LinkData linkData : linkDataList) {
            int from = linkData.getFrom();
            int to = linkData.getTo();
            if (nodeKey == from) {
                connections.add(to);
            }
            if (nodeKey == to) {
                connections.add(from);
            }
        }
        return connections;
    }

    private Integer findSpecificNodeConnectionsOnlyFrom(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        Integer connections = 0;

        for (LinkData linkData : linkDataList) {
            int from = linkData.getFrom();
            int to = linkData.getTo();
            if (nodeKey == to) {
                connections = from;

            }
        }
        return connections;
    }



    private List<Map.Entry<Integer, List<Integer>>> detectAllRoundedRectanglesWithRelations(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        Map<Integer, Integer> correlationsBetweenNodes = new HashMap<>();
        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("RoundedRectangle")) {
                List<Integer> specificNodeConnections = findSpecificNodeConnectionsToAndFrom(importDialogParser, nodeData.getKey());
                Integer specificNodeConnectionsOnlyTo = findSpecificNodeConnectionsOnlyFrom(importDialogParser, nodeData.getKey());
                correlationsBetweenNodes.put(nodeData.getKey(), specificNodeConnectionsOnlyTo);
            }
        }

        List<Map.Entry<Integer, List<Integer>>> collect = correlationsBetweenNodes.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())))
                .entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toList());

        return collect;
    }


    private List<Integer> detectAllRoundedRectanglesNodes(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        List<Integer> nodesKeys = new ArrayList<>();
        for (NodeData nodeData : nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("RoundedRectangle")) {
                nodesKeys.add(nodeData.getKey());
            }
        }
        return nodesKeys;
    }

    private void mapImportParserIntoOurDatabaseStructure(ImportDialogParser importDialogParser) {


        System.out.println("Mapping started! ");

        ImportDialogParser importDialogParserWithoutKeyboardCandidates = excludeRectangularsAndCirclesFromPool(importDialogParser);

        List<NodeData> nodeDataList = importDialogParserWithoutKeyboardCandidates.getNodeDataList();
        List<Integer> nodesKeys = convertNodeArrayIntoNodeKeyArray(nodeDataList);
        for (Integer key: nodesKeys) {
            boolean isItBeginningOfTheBranch = isItBeginningOfTheBranch(importDialogParserWithoutKeyboardCandidates, key);
            log.info("Is " + key + " beginning of the branch? " + isItBeginningOfTheBranch);

            boolean isItEndingOfTheBranch = isItEndingOfTheBranch(importDialogParserWithoutKeyboardCandidates, key);
            log.info("Is " + key + " ending of the branch?    " + isItEndingOfTheBranch);

            String valueFromNodeKey = getValueFromNodeKey(importDialogParserWithoutKeyboardCandidates, key);
            log.info("value" + valueFromNodeKey);
            log.info("");
        }
    }

    private ImportDialogParser excludeRectangularsAndCirclesFromPool(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        nodeDataList.removeIf(nodeData -> nodeData.getFigure() != null && (nodeData.getFigure().equals("RoundedRectangle") || nodeData.getFigure().equals("Circle")));
        importDialogParser.setNodeDataList(nodeDataList);
        return importDialogParser;
    }


    private boolean isItBeginningOfTheBranch(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        int counter = 0;

        for (LinkData linkData : linkDataList) {
            if (linkData.getFrom() == nodeKey) {
                counter++;
            }
        }
        return counter >= 2;
    }

    private boolean isItEndingOfTheBranch(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        boolean isItBeginningOfTheBranch = false;
        for (LinkData linkData : linkDataList) {
            if (linkData.getFrom() == nodeKey) {
                isItBeginningOfTheBranch = isItBeginningOfTheBranch(importDialogParser, linkData.getFrom());
            }
        }
        return !isItBeginningOfTheBranch;
    }

    private List<Integer> convertNodeArrayIntoNodeKeyArray(List<NodeData> nodeDataList) {

        List<Integer> nodeKeyArray = new ArrayList<>();
        for (NodeData nodeData: nodeDataList) {
            nodeKeyArray.add(nodeData.getKey());
        }
        return nodeKeyArray;
    }

    private String getValueFromNodeKey(ImportDialogParser importDialogParser, Integer nodeKey) {

        String nodeValue = StringUtils.EMPTY;
        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getKey() == nodeKey) {
                nodeValue = nodeData.getText();
            }
        }
        return nodeValue;
    }
}

