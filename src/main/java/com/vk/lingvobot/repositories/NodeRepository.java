package com.vk.lingvobot.repositories;

import com.vk.lingvobot.entities.Node;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NodeRepository extends JpaRepository<Node, Integer> {

    @Query("SELECT n FROM Node n WHERE n.nodeKey = :nodeKey AND n.dialog.dialogId = :dialogId")
    Node findByNodeKey(@Param("nodeKey") int nodeKey, @Param("dialogId") int dialogId);

    @Query("SELECT n FROM Node n WHERE n.nodeId = :nodeId")
    Node findByNodeId(@Param("nodeId") int nodeId);
}
