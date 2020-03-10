package com.vk.lingvobot.services.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.DialogPhrase;
import com.vk.lingvobot.entities.DialogState;
import com.vk.lingvobot.parser.importDialogParser.ImportDialogParser;
import com.vk.lingvobot.parser.importDialogParser.LinkData;
import com.vk.lingvobot.parser.importDialogParser.NodeData;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.services.ImportDialogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class ImportDialogServiceImpl implements ImportDialogService {

    @Autowired
    private DialogRepository dialogRepository;

    private Gson gson = new GsonBuilder().create();

    @Autowired
    private ImportDialogParser importDialogParser;

    public ImportDialogParser getImportDialogParser() {
        return importDialogParser;
    }

    @Autowired
    ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
/*
        System.out.println("Import............................");
*/
        Path path = Paths.get("D:/Projects/test.txt");
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
        this.importDialogParser = importDialogData;
        ImportDialogParser importDialogDataWithPositiveValues = convertNodesIntoPositiveValues(importDialogData);
        this.importDialogParser = squashAllKeyboardsCandidates(importDialogData);
        importDialogParserLocationProblem(this.importDialogParser);
        importDialogParserIntoOurStructure(importDialogParser);
        System.out.println("let's see");
    }

    public ImportDialogParser convertImportDialogParserIntoAdequateFormat(ImportDialogParser importDialogParser) {

        ImportDialogParser importDialogParserAdequateFormat = squashAllKeyboardsCandidates(importDialogParser);
        return importDialogParserAdequateFormat;
    }

    public DialogState createSimpleChainElementWithoutBranching(Dialog dialog, int i, NodeData nodeData, int stateCounter) {

        DialogState dialogState = new DialogState();
        dialogState.setDialog(dialog);
        dialogState.setState(i + 1);
        stateCounter++;

            DialogPhrase dialogPhrase = new DialogPhrase();
            dialogPhrase.setDialogPhraseValue(nodeData.getText());

        dialogState.setDialogPhrase(dialogPhrase);

        return dialogState;
    }

    void dealWithBranching() {

    }


    public void importDialogParserIntoOurStructure(ImportDialogParser importDialogParser) {
        System.out.println("importDialogParserIntoOurStructure is calling dude! ");
        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();

        Dialog dialog = new Dialog();
        dialog.setDialogName("TestOne");

        int stateCounter = 1;

        for (int i = 0; i < nodeDataList.size(); i++) {
            NodeData nodeData = nodeDataList.get(i);

            boolean isItBeginningOfTheBranch = isItBeginningOfTheBranch(importDialogParser, nodeData.getKey());
            boolean isItEndingOfTheBranch = isItEndingOfTheBranch(importDialogParser, nodeData.getKey());

            System.out.println("branch " + i + " begin? " + isItBeginningOfTheBranch + " end? " + isItEndingOfTheBranch);
            if (!isItBeginningOfTheBranch) {
                createSimpleChainElementWithoutBranching(dialog, i, nodeData, stateCounter);
            } else {
                createSimpleChainElementWithoutBranching(dialog, i, nodeData, stateCounter);

            }

        }
        nodeDataList.forEach(System.out::println);

    }

    public ImportDialogParser importDialogParserLocationProblem(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        Collections.sort(nodeDataList);
        Set<Triple<Integer, Integer, Boolean>> tripleSet = new HashSet<>();
        for (int i = 0; i < nodeDataList.size(); i++) {
            NodeData nodeData = nodeDataList.get(i);
            int key = nodeData.getKey();
            nodeData.setKey(i);
//            System.out.println("For node with key " + key + " value is switched to " + nodeData.getKey());

            for (int j = 0; j < linkDataList.size(); j++) {
                int from = linkDataList.get(j).getFrom();
                int to = linkDataList.get(j).getTo();

                Triple tripleFrom = new ImmutableTriple(j, "from", true);
                Triple tripleTo = new ImmutableTriple(j, "to", true);
                if (from == key && !tripleSet.contains(tripleFrom)) {
                    linkDataList.get(j).setFrom(nodeData.getKey());
                    tripleSet.add(new ImmutableTriple(j , "from" , true));

                }
                if (to == key && !tripleSet.contains(tripleTo)) {
                    tripleSet.add(new ImmutableTriple(j , "to" , true));
                    linkDataList.get(j).setTo(nodeData.getKey());
                }
            }
        }
        return importDialogParser;
    }

    public void importDialog() {

        detectAllRoundedRectanglesWithRelations(); //<--This is for future keyboard feature


        /*mapImportParserIntoOurDatabaseStructure();*/
    }


    public ImportDialogParser convertNodesIntoPositiveValues(ImportDialogParser importDialogParser) {

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
     * @return
     */
    public ImportDialogParser squashAllKeyboardsCandidates(ImportDialogParser importDialogParser) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        ImportDialogParser importDialogDataWithPositiveValues = convertNodesIntoPositiveValues(importDialogParser);
        List<Map.Entry<Integer, List<Integer>>> entries = detectAllRoundedRectanglesWithRelations();
        for (Map.Entry<Integer, List<Integer>> entry : entries) {
            List<Integer> value = entry.getValue();
            for (Integer it : value) {
                List<Integer> specificNodeConnectionsToAndFrom = findSpecificNodeConnectionsToAndFrom(it);
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



    public void findAllConnections() {

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

    public List<Integer> findSpecificNodeConnectionsToAndFrom(int nodeKey) {

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

    public Integer findSpecificNodeConnectionsOnlyFrom(int nodeKey) {

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

    public Set<Integer> findSpecificNodeConnectionsOnlyTo(int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        Set<Integer> connections = new HashSet<>();

        for (LinkData linkData : linkDataList) {
            int from = linkData.getFrom();
            int to = linkData.getTo();
            if (nodeKey == from) {
                connections.add(to);

            }
        }
        return connections;
    }



    public List<Map.Entry<Integer, List<Integer>>> detectAllRoundedRectanglesWithRelations() {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        Map<Integer, Integer> correlationsBetweenNodes = new HashMap<>();
        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("RoundedRectangle")) {
                List<Integer> specificNodeConnections = findSpecificNodeConnectionsToAndFrom(nodeData.getKey());
                Integer specificNodeConnectionsOnlyTo = findSpecificNodeConnectionsOnlyFrom(nodeData.getKey());
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


    public List<Integer> detectAllRoundedRectanglesNodes(ImportDialogParser importDialogParser) {

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




    public ImportDialogParser excludeRectangularsFromPool(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        nodeDataList.removeIf(nodeData -> nodeData.getFigure() != null && (nodeData.getFigure().equals("RoundedRectangle")));
        importDialogParser.setNodeDataList(nodeDataList);
        return importDialogParser;
    }

    public NodeData findStart(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        NodeData nodeDataWeNeed = null;

        for (NodeData nodeData : nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("Circle") && nodeData.getFill().equals("#00AD5F")) {
                nodeDataWeNeed = nodeData;
            }
        }
        return nodeDataWeNeed;
    }


    public boolean isItBeginningOfTheBranch(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        int counter = 0;

        for (LinkData linkData : linkDataList) {
            if (linkData.getFrom() == nodeKey) {
                counter++;
            }
        }
        return counter >= 2;
    }

    public boolean isItEndingOfTheBranch(ImportDialogParser importDialogParser, int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        int counter = 0;
        Map<Integer, List<Integer>> toDuplicates = new HashMap<>();

        for (LinkData linkData : linkDataList) {
            if (!toDuplicates.containsKey(linkData.getTo())) {
                List<Integer> a = new ArrayList<>();
                a.add(linkData.getFrom());
                toDuplicates.put(linkData.getTo(), a);
            } else {
                List<Integer> integers = toDuplicates.get(linkData.getTo());
                integers.add(linkData.getFrom());
                toDuplicates.put(linkData.getTo(), integers);
            }
        }

        for (Map.Entry<Integer, List<Integer>> entry: toDuplicates.entrySet()) {
            List<Integer> value = entry.getValue();
            if (value.contains(nodeKey) && value.size() > 1) return true;
        }
        return false;
        /*boolean isItBeginningOfTheBranch = false;
        for (LinkData linkData : linkDataList) {
            if (linkData.getFrom() == nodeKey) {
                isItBeginningOfTheBranch = isItBeginningOfTheBranch(importDialogParser, linkData.getFrom());
            }
        }
        return !isItBeginningOfTheBranch;*/


    }

    public List<Integer> convertNodeArrayIntoNodeKeyArray(List<NodeData> nodeDataList) {

        List<Integer> nodeKeyArray = new ArrayList<>();
        for (NodeData nodeData: nodeDataList) {
            nodeKeyArray.add(nodeData.getKey());
        }
        return nodeKeyArray;
    }

    public String getValueFromNodeKey(ImportDialogParser importDialogParser, Integer nodeKey) {

        String nodeValue = StringUtils.EMPTY;
        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getKey() == nodeKey) {
                nodeValue = nodeData.getText();
            }
        }
        return nodeValue;
    }

    public NodeData getNodeFromNodeKey(Integer nodeKey) {
        NodeData nodeDataWeFind = null;
        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getKey() == nodeKey) {
                nodeDataWeFind = nodeData;
            }
        }
        return nodeDataWeFind;
    }

    public List<NodeData> findNextNode(NodeData currentNode) {

        Set<Integer> specificNodeConnectionsOnlyTo = findSpecificNodeConnectionsOnlyTo(currentNode.getKey());
        List<NodeData> nodeDataWeFind = new ArrayList<>();
        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        for (NodeData nodeData: nodeDataList) {
            for (Integer integer: specificNodeConnectionsOnlyTo) {
                if (nodeData.getKey() == integer) {
                    nodeDataWeFind.add(nodeData);
                }
            }
        }
        return nodeDataWeFind;
    }

   /* @NoArgsConstructor
    @DependsOn({"importDialogServiceImpl"})
    @Data
    class Node {

        private String text;
        private Integer key;
        private List<Node> nodeNextList;

        public Node(String text, Integer key, Node nodeNext) {
            this.text = text;
            this.key = key;

            List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
            Integer keyOfNextNode = nodeNext.getKey();
            NodeData nodeFromNodeKey = getNodeFromNodeKey(keyOfNextNode);

            List<NodeData> nextNode = findNextNode(nodeFromNodeKey);
            for (NodeData nodeData: nextNode) {
                Node node = new Node(nodeData.getText(), nodeData.getKey(), null);
            }
            System.out.println("non NPE " + nextNode.toString());
        }
    }*/

}



