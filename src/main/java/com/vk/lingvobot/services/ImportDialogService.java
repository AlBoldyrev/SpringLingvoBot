package com.vk.lingvobot.services;

import com.vk.lingvobot.entities.Dialog;
import com.vk.lingvobot.entities.DialogState;
import com.vk.lingvobot.parser.importDialogParser.ImportDialogParser;
import com.vk.lingvobot.parser.importDialogParser.NodeData;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public interface ImportDialogService {

    ImportDialogParser getImportDialogParser();

    void init();


    ImportDialogParser convertImportDialogParserIntoAdequateFormat(ImportDialogParser importDialogParser);

    DialogState createSimpleChainElementWithoutBranching(Dialog dialog, int i, NodeData nodeData);

    void importDialogParserIntoOurStructure(ImportDialogParser importDialogParser);

    //Распределить NodeData в исходном json в правильном порядке (была проблема с тем, что если мы создаем наш json в произвольном порядке, то NodeData могут иметь случайные NodeKey, этот метод просто всё упорядочивает
    ImportDialogParser importDialogParserLocationProblem(ImportDialogParser importDialogParser);

    void importDialog();

    //Сделать ключи в исходном json положительными для дальнейшего удобства
    ImportDialogParser convertNodesIntoPositiveValues(ImportDialogParser importDialogParser);

    //Очистить исходный json от "клавиатурных" nodeData
    ImportDialogParser squashAllKeyboardsCandidates(ImportDialogParser importDialogParser);

    void findAllConnections();

    List<Integer> findSpecificNodeConnectionsToAndFrom(int nodeKey);

    //Получить список ключей предыдущих NodeData относительно текущей
    Integer findSpecificNodeConnectionsOnlyFrom(int nodeKey);

    //Получить список ключей следующий NodeData относительно текущей
    Set<Integer> findSpecificNodeConnectionsOnlyTo(int nodeKey);

    //Получить список из ключей NodeData со связями
    List<Map.Entry<Integer, List<Integer>>> detectAllRoundedRectanglesWithRelations();

    //Получить список всех "Клавиатурных" NodeData
    List<Integer> detectAllRoundedRectanglesNodes(ImportDialogParser importDialogParser);

    //Удалить все "Клавиатурные" NodeData
    ImportDialogParser excludeRectangularsFromPool(ImportDialogParser importDialogParser);

    //Найти стартовую NodeData
    NodeData findStart(ImportDialogParser importDialogParser);

    //Является ли NodeData началом ветки
    boolean isItBeginningOfTheBranch(ImportDialogParser importDialogParser, int nodeKey);

    //Является ли NodeData концом ветки?
    boolean isItEndingOfTheBranch(ImportDialogParser importDialogParser, int nodeKey);

    //Из списка NodeData получить список ключей NodeData
    List<Integer> convertNodeArrayIntoNodeKeyArray(List<NodeData> nodeDataList);

    //По ключу получить значение NodeData
    String getValueFromNodeKey(ImportDialogParser importDialogParser, Integer nodeKey);

    //По ключу получить NodeData
    NodeData getNodeFromNodeKey(Integer nodeKey);

    //Возвращает одну или несколько NodeData, которые связаны с текущей
    List<NodeData> findNextNode(NodeData currentNode);
}
