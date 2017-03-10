package org.hammer.yml;
 
 

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(
        locations = "classpath:wfl.yml")
public class WflProperties {	
	private List<WflOrder> wflOrder ;

 


	public List<WflOrder> getWflOrder() {
		return wflOrder;
	}


	public void setWflOrder(List<WflOrder> wflOrder) {
		this.wflOrder = wflOrder;
	}


	public static class WflStep{
		
		Integer stepId ;
		
		String stepCode ;
		
		String stepType ;
		
		String stepName ;
		
		Boolean isAuto ;

		public Integer getStepId() {
			return stepId;
		}

		public void setStepId(Integer stepId) {
			this.stepId = stepId;
		}

		public String getStepCode() {
			return stepCode;
		}

		public void setStepCode(String stepCode) {
			this.stepCode = stepCode;
		}

		public String getStepType() {
			return stepType;
		}

		public void setStepType(String stepType) {
			this.stepType = stepType;
		}

		public String getStepName() {
			return stepName;
		}

		public void setStepName(String stepName) {
			this.stepName = stepName;
		}

		public Boolean getIsAuto() {
			return isAuto;
		}

		public void setIsAuto(Boolean isAuto) {
			this.isAuto = isAuto;
		}
		
		
	
	}

	public static class WflControl{
		private Integer controlId ;
		
		private Integer stepId ;
		
		private Integer nextStepId ;

		public Integer getControlId() {
			return controlId;
		}

		public void setControlId(Integer controlId) {
			this.controlId = controlId;
		}

		public Integer getStepId() {
			return stepId;
		}

		public void setStepId(Integer stepId) {
			this.stepId = stepId;
		}

		public Integer getNextStepId() {
			return nextStepId;
		}

		public void setNextStepId(Integer nextStepId) {
			this.nextStepId = nextStepId;
		}
		
		
	}


	public static class WflOrder {
		
		private Integer wflId ;
		
		private String wflName ;
		
		private String wflCode ;		

		private List<WflControl> wflControls ;

		private List<WflStep> wflSteps ;		
	 
		public List<WflStep> getWflSteps() {
			return wflSteps;
		}

		public void setWflSteps(List<WflStep> wflSteps) {
			this.wflSteps = wflSteps;
		}

		public List<WflControl> getWflControls() {
			return wflControls;
		}

		public void setWflControls(List<WflControl> wflControls) {
			this.wflControls = wflControls;
		}

		public Integer getWflId() {
			return wflId;
		}

		public void setWflId(Integer wflId) {
			this.wflId = wflId;
		}

		public String getWflName() {
			return wflName;
		}

		public void setWflName(String wflName) {
			this.wflName = wflName;
		}

		public String getWflCode() {
			return wflCode;
		}

		public void setWflCode(String wflCode) {
			this.wflCode = wflCode;
		}
		
		
	}
}
