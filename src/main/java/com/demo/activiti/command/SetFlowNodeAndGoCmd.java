package com.demo.activiti.command;

import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.impl.interceptor.Command;
import org.activiti.engine.impl.interceptor.CommandContext;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

/**
 * 根据提供节点和执行对象id，进行跳转命令.
 */
public class SetFlowNodeAndGoCmd implements Command<Void> {
	private FlowNode tagetElement;
	private FlowNode sourceElement;
	private String executionId;

	public SetFlowNodeAndGoCmd(FlowNode sourceElement, FlowNode tagetElement, String executionId) {
		this.sourceElement = sourceElement;
		this.tagetElement = tagetElement;
		this.executionId = executionId;
	}

	@Override
	public Void execute(CommandContext commandContext) {
		SequenceFlow sequenceFlow = new SequenceFlow();
		sequenceFlow.setId("tempSequenceFlowId" + System.currentTimeMillis());
		sequenceFlow.setSourceFlowElement(sourceElement);
		sequenceFlow.setTargetFlowElement(tagetElement);
		ExecutionEntity executionEntity = commandContext.getExecutionEntityManager().findById(executionId);
		executionEntity.setCurrentFlowElement(sequenceFlow);
		commandContext.getAgenda().planTakeOutgoingSequenceFlowsOperation(executionEntity, true);
		return null;
	}
}
