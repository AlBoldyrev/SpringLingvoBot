package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.NodeNext;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NodeNextRepository extends JpaRepository<NodeNext, Integer> {

    @Query("SELECT nn FROM NodeNext nn WHERE nn.nodeId = :nodeId AND nn.dialog.dialogId = :dialogId")
    List<NodeNext> findByNodeIdNextNodes(@Param("nodeId") int nodeId, @Param("dialogId") int dialogId);

    @Query("SELECT nn FROM NodeNext nn WHERE nn.keyboardValue = :keyboardValue AND nn.nodeId = :nodeId")
    NodeNext findByKeyboardValue(@Param("keyboardValue") String keyboardValue, @Param("nodeId") int nodeId);
}
