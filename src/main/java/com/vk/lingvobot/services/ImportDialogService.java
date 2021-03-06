package com.vk.lingvobot.services;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.Node;
import com.vk.lingvobot.entities.NodeNext;
import com.vk.lingvobot.parser.importDialogParser.ImportDialogParser;
import com.vk.lingvobot.parser.importDialogParser.LinkData;
import com.vk.lingvobot.parser.importDialogParser.NodeData;
import com.vk.lingvobot.repositories.DialogRepository;
import com.vk.lingvobot.repositories.NodeNextRepository;
import com.vk.lingvobot.repositories.NodeRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ImportDialogService {

    private final DialogRepository dialogRepository;
    private ImportDialogParser importDialogParser;
    private final NodeRepository nodeRepository;
    private final NodeNextRepository nodeNextRepository;

    private Gson gson = new GsonBuilder().create();

    public ImportDialogParser getImportDialogParser() {
        return importDialogParser;
    }

    @Transactional
    public void importDialog(byte[] bytes) throws Exception {

        String dialogInJson = null;

        dialogInJson = new String(bytes, StandardCharsets.UTF_8);

        ImportDialogParser importDialogData = gson.fromJson(dialogInJson, ImportDialogParser.class);
        this.importDialogParser = importDialogData;
        ImportDialogParser importDialogDataWithPositiveValues = convertNodesIntoPositiveValues(importDialogData);
        importDialogParserLocationProblem(importDialogParser);
        List<KeyboardRectangular> keyboardRectangularList = keyboardRectangulars(importDialogParser);
        dealWithKeyboard(importDialogParser, keyboardRectangularList);

        saveStructureToDatabase(importDialogParser, keyboardRectangularList);


    }



    private void saveStructureToDatabase(ImportDialogParser importDialogParser, List<KeyboardRectangular> keyboardRectangularList) throws Exception {

        //TODO NPE
        NodeData dialogNameNodeData = null;
        try {
           dialogNameNodeData = findDialogNameNodeData(importDialogParser);
        } catch (Exception e) {
            log.error("No dialog name in schema! ");
        }
        String dialogName;
        if (dialogNameNodeData != null) {
            dialogName = dialogNameNodeData.getText();
        } else {
            throw new Exception();
        }

        Dialog dialog = dialogRepository.findByDialogName(dialogName);
        if (dialog == null) {
            dialog = new Dialog();
            dialog.setDialogName(dialogName);
        }


        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();

        List<Node> nodesForSavingToDB = new ArrayList<>();
        List<NodeNext> nextNodesForSavingToDB = new ArrayList<>();

        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getFigure() == null || nodeData.getFigure().equals("Circle") ) {
                int key = nodeData.getKey();
                String text = nodeData.getText();
                Node node = new Node();
                node.setDialog(dialog);
                node.setNodeKey(nodeData.getKey());
                node.setNodeValue(nodeData.getText());
                nodesForSavingToDB.add(node);

                Set<Integer> nextNodes = findSpecificNodeConnectionsOnlyTo(nodeData.getKey());

                for (Integer nextNodekey: nextNodes) {

                    List<String> keyboardValues = getKeyboardValueConnectedWithNode(keyboardRectangularList, nextNodekey);

                    if (!keyboardValues.isEmpty()) {
                        for (String keyboardValue : keyboardValues) {

                                //TODO Обработка сообщения о том, что не надо делать длину клавиатуры больше 40 символов. Выкинуть другое кастомное исключение наружу и там обработать.


                            NodeData nextNode = getNodeFromNodeKey(nextNodekey);
                            NodeNext nodeNext = new NodeNext();
                            nodeNext.setDialog(dialog);
                            nodeNext.setNodeId(key);
                            //TODO setNextNode
                            nodeNext.setNextNode(nextNodekey);
                            nodeNext.setKeyboardValue(keyboardValue.trim());
                            nextNodesForSavingToDB.add(nodeNext);
                        }
                    } else {
                        NodeData nextNode = getNodeFromNodeKey(nextNodekey);
                        NodeNext nodeNext = new NodeNext();
                        nodeNext.setDialog(dialog);
                        nodeNext.setNodeId(key);
                        //TODO setNextNode
                        nodeNext.setNextNode(nextNodekey);
                        nodeNext.setKeyboardValue(null);
                        nextNodesForSavingToDB.add(nodeNext);
                    }

                }


            }
        }
        dialogRepository.save(dialog);
        nodeRepository.saveAll(nodesForSavingToDB);
        nodeNextRepository.saveAll(nextNodesForSavingToDB);

    }


    private List<String> getKeyboardValueConnectedWithNode(List<KeyboardRectangular> keyboardRectangularList, Integer nodeKey) {

        List<String> keyboardValues = new ArrayList<>();
        for (KeyboardRectangular keyboardRectangular: keyboardRectangularList) {
            if (keyboardRectangular.getTo().contains(nodeKey)) {
                keyboardValues.add(keyboardRectangular.getValue());
            }
        }
        return keyboardValues;
    }

    private NodeData getNodeFromNodeKey(Integer nodeKey) {
        NodeData nodeDataWeFind = null;
        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getKey() == nodeKey) {
                nodeDataWeFind = nodeData;
            }
        }
        return nodeDataWeFind;
    }

    private NodeData findDialogNameNodeData(ImportDialogParser importDialogParser) throws Exception {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        for (NodeData nodeData : nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("Database")) {
                return nodeData;
            }
        }
        throw new Exception("No dialog name in this schema! ");
    }

    private List<KeyboardRectangular> keyboardRectangulars (ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        List<NodeData> onlyKeyboards = new ArrayList<>();
        List<KeyboardRectangular> keyboardRectangularList = new ArrayList<>();

        for (NodeData nodeData: nodeDataList) {
            if (nodeData.getFigure() != null && nodeData.getFigure().equals("RoundedRectangle")) {
                onlyKeyboards.add(nodeData);
            }
        }

        for (NodeData nodeData: onlyKeyboards) {
            Set<Integer> specificNodeConnectionsOnlyTo = findSpecificNodeConnectionsOnlyTo(nodeData.getKey());
            Set<Integer> specificNodeConnectionsOnlyFrom = findSpecificNodeConnectionsOnlyFrom(nodeData.getKey());

            KeyboardRectangular keyboardRectangular = new KeyboardRectangular(nodeData.getKey(), nodeData.getText(), specificNodeConnectionsOnlyFrom, specificNodeConnectionsOnlyTo);
            keyboardRectangularList.add(keyboardRectangular);

        }
        return keyboardRectangularList;

    }



    private ImportDialogParser dealWithKeyboard(ImportDialogParser importDialogParser, List<KeyboardRectangular> keyboardRectangularList) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        //TODO lambda
        List<Integer> keyboardsKey = new ArrayList<>();
        List<LinkData> linkDataListForRemoval = new ArrayList<>();
        for (KeyboardRectangular keyboardRectangular: keyboardRectangularList) {
            int key = keyboardRectangular.getKey();
            keyboardsKey.add(key);
        }

        for (Integer key: keyboardsKey) {
            for (LinkData linkData : linkDataList) {
                if (linkData.getFrom() == key || linkData.getTo() == key) {
                    linkDataListForRemoval.add(linkData);
                }
            }
        }

        linkDataList.removeAll(linkDataListForRemoval);

        for (KeyboardRectangular keyboardRectangular: keyboardRectangularList) {
            Set<Integer> fromSet = keyboardRectangular.getFrom();
            Set<Integer> toSet = keyboardRectangular.getTo();

            for (Integer to: toSet) {
                for (Integer from: fromSet) {
                    LinkData linkData = new LinkData();
                    linkData.setFrom(from);
                    linkData.setTo(to);
                    linkDataList.add(linkData);
                }
            }
        }

        importDialogParser.setLinkDataList(linkDataList);
        importDialogParser.setNodeDataList(nodeDataList);
        return importDialogParser;
    }


    private ImportDialogParser importDialogParserLocationProblem(ImportDialogParser importDialogParser) {

        List<NodeData> nodeDataList = importDialogParser.getNodeDataList();
        List<LinkData> linkDataList = importDialogParser.getLinkDataList();
        Collections.sort(nodeDataList);
        Set<Triple<Integer, Integer, Boolean>> tripleSet = new HashSet<>();
        for (int i = 0; i < nodeDataList.size(); i++) {
            NodeData nodeData = nodeDataList.get(i);
            int key = nodeData.getKey();
            nodeData.setKey(i);

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

    private Set<Integer> findSpecificNodeConnectionsOnlyFrom(int nodeKey) {

        List<LinkData> linkDataList = importDialogParser.getLinkDataList();

        Set<Integer> connections = new HashSet<>();

        for (LinkData linkData : linkDataList) {
            int from = linkData.getFrom();
            int to = linkData.getTo();
            if (nodeKey == to) {
                connections.add(from);

            }
        }
        return connections;
    }

    private Set<Integer> findSpecificNodeConnectionsOnlyTo(int nodeKey) {

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
}

@Data
@AllArgsConstructor
@Slf4j
class KeyboardRectangular {

    private int key;
    private String value;
    private Set<Integer> from;
    private Set<Integer> to;

}