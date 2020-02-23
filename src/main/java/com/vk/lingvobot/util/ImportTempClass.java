package com.vk.lingvobot.util;

import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.DialogState;
import com.vk.lingvobot.parser.importDialogParser.ImportDialogParser;
import com.vk.lingvobot.parser.importDialogParser.NodeData;

import java.util.List;

public class ImportTempClass {

    /*private void mapImportParserIntoOurDatabaseStructure() {


        System.out.println("Mapping started! ");

        ImportDialogParser importDialogParserWithoutKeyboardCandidates = excludeRectangularsFromPool(importDialogParser);

        List<NodeData> nodeDataList = importDialogParserWithoutKeyboardCandidates.getNodeDataList();
        List<Integer> nodesKeys = convertNodeArrayIntoNodeKeyArray(nodeDataList);

        Dialog newDialogForImport = new Dialog();
        DialogState dialogState = new DialogState();
        dialogState.setDialog(newDialogForImport);

        NodeData start = findStart(importDialogParser);
        System.out.println("We find START: " + start.getKey() + " with text: " + start.getText());

        NodeData nodeConnectedWithStart = findNextNode(start).get(0);
        String valueFromNodeKey = getValueFromNodeKey(importDialogParser, nodeConnectedWithStart.getKey());
        System.out.println("We find node connected with start: " + nodeConnectedWithStart.getKey() + " with value: " + nodeConnectedWithStart.getText());


        *//*Node nodeStart = new Node();
        nodeStart.setKey(nodeConnectedWithStart.getKey());
        nodeStart.setText(nodeConnectedWithStart.getText());
        nodeStart.setNodeNextList(null);

        Node node = new Node(nodeStart.getText(), nodeStart.getKey(), nodeStart);*//*


        *//*for (NodeData nodeData: nodeDataList) {
            int key = nodeData.getKey();
            boolean isItBeginningOfTheBranch = isItBeginningOfTheBranch(importDialogParserWithoutKeyboardCandidates, key);
            boolean isItEndingOfTheBranch = isItEndingOfTheBranch(importDialogParserWithoutKeyboardCandidates, key);
            List<NodeData> nextNode = findNextNode(importDialogParser, nodeData);
            if (nextNode.size() == 0) {
                System.out.println("fucking mistake");
            }
            if (nextNode.size() == 1) {
                System.out.println("We find node connected with Node " + nodeData.getKey() + ". It is node " + nextNode.get(0).getKey() + " with value: " + nextNode.get(0).getText());
            }
            if (nextNode.size() > 1) {
                List<Integer> integers = nextNode.stream().map(NodeData::getKey).collect(Collectors.toList());
                System.out.println("We find node connected with Node " + nodeData.getKey() + ". It is nodes " + integers.toString());
            }
        }*//*

        dialogRepository.save(newDialogForImport);


    }*/

}
