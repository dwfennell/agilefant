package fi.hut.soberit.agilefant.web;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import fi.hut.soberit.agilefant.annotations.PrefetchId;
import fi.hut.soberit.agilefant.business.StoryHierarchyBusiness;
import fi.hut.soberit.agilefant.business.TaskBusiness;
import fi.hut.soberit.agilefant.business.TransferObjectBusiness;
import fi.hut.soberit.agilefant.model.Story;
import fi.hut.soberit.agilefant.model.Task;
import fi.hut.soberit.agilefant.model.User;
import fi.hut.soberit.agilefant.model.User.UserSettingType;
import fi.hut.soberit.agilefant.security.SecurityUtil;
import fi.hut.soberit.agilefant.util.HourEntryHandlingChoice;

@Component("taskAction")
@Scope("prototype")
public class TaskAction extends ActionSupport implements Prefetching, CRUDAction {

    private static final long serialVersionUID = 7699657599039468223L;
    
    // Services
    @Autowired
    private TaskBusiness taskBusiness;
    
    @Autowired
    private TransferObjectBusiness transferObjectBusiness;
    
    @Autowired
    private StoryHierarchyBusiness storyHierarchyBusiness;
    
    // Helper fields
    private Task task;
    @PrefetchId
    private int taskId;
    
    private int rankUnderId;
    
    private Integer iterationId;
    private Integer storyId;
    private boolean responsiblesChanged = false;
    private boolean storyToStarted = false;
    
    private Story parentStory;
    
    private Set<User> newResponsibles = new HashSet<User>();
    
    private HourEntryHandlingChoice hourEntryHandlingChoice;
    

    // CRUD
    public String create() {
        setTask(new Task());
        return Action.SUCCESS;
    }
    
    public String store() {
        if (responsiblesChanged) {
            task.setResponsibles(newResponsibles);
        }
        
        boolean startStory = false;
        
        if (storyToStarted || markStoryStarted()) {
            startStory = true;
        }
        
        task = taskBusiness.storeTask(task, iterationId, storyId, startStory);
        taskToTransferObject();
        if (startStory) {
            return Action.SUCCESS + "_withStory";
        }
        return Action.SUCCESS;
    }
    
    private boolean markStoryStarted() {
        User currentUser = SecurityUtil.getLoggedUser();
        if (currentUser != null 
                && currentUser.getMarkStoryStarted() == UserSettingType.always) {
            return true;
        }
        return false;
    }
    
    public String retrieve() {
        task = taskBusiness.retrieve(taskId);
        taskToTransferObject();
        return Action.SUCCESS;
    }
    
    public String delete() {
        taskBusiness.deleteAndUpdateHistory(taskId, hourEntryHandlingChoice);
        return Action.SUCCESS;
    }
    
    // OTHER FUNCTIONS
    
    public String getTaskContext() {
        task = taskBusiness.retrieve(taskId);
        if (task.getStory() != null) {
            parentStory = storyHierarchyBusiness.recurseHierarchy(task.getStory());
        }
        return Action.SUCCESS;
    }
    
    public String move() {
        task = taskBusiness.retrieve(taskId);
        task = taskBusiness.move(task, iterationId, storyId);
        taskToTransferObject();
        return Action.SUCCESS;
    }
    
    public String resetOriginalEstimate() {
        task = taskBusiness.retrieve(taskId);
        task = taskBusiness.resetOriginalEstimate(taskId);
        taskToTransferObject();
        return Action.SUCCESS;
    }
    
    public String rankUnder() {
        task = taskBusiness.retrieve(taskId);
        Task rankUnder = taskBusiness.retrieveIfExists(rankUnderId);
        
        task = taskBusiness.rankAndMove(task, rankUnder, storyId, iterationId);
        
        return Action.SUCCESS;
    }
    
    public String deleteTaskForm() {
        task = taskBusiness.retrieve(taskId);
        return Action.SUCCESS;
    }
    
    private void taskToTransferObject() {
        task = transferObjectBusiness.constructTaskTO(task);
    }
        
    // Prefetching
    public void initializePrefetchedData(int objectId) {
        task = taskBusiness.retrieveDetached(objectId);
    }
    
      
    // AUTOGENERATED    
    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }
    
    public void setTaskBusiness(TaskBusiness taskBusiness) {
        this.taskBusiness = taskBusiness;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void setTransferObjectBusiness(
            TransferObjectBusiness transferObjectBusiness) {
        this.transferObjectBusiness = transferObjectBusiness;
    }

    public void setIterationId(Integer iterationId) {
        this.iterationId = iterationId;
    }

    public void setStoryId(Integer storyId) {
        this.storyId = storyId;
    }
    
    public Set<User> getNewResponsibles() {
        return this.newResponsibles;
    }

    public void setNewResponsibles(Set<User> newResponsibles) {
        this.newResponsibles = new HashSet<User>(newResponsibles);
    }

    public void setRankUnderId(int rankUnderId) {
        this.rankUnderId = rankUnderId;
    }
    
    public void setHourEntryHandlingChoice(
            HourEntryHandlingChoice hourEntryHandlingChoice) {
        this.hourEntryHandlingChoice = hourEntryHandlingChoice;
    }
    public HourEntryHandlingChoice getHourEntryHandlingChoice() {
        return hourEntryHandlingChoice;
    }
    
    public void setResponsiblesChanged(boolean responsiblesChanged) {
        this.responsiblesChanged = responsiblesChanged;
    }

    public Story getParentStory() {
        return parentStory;
    }

    public void setStoryToStarted(boolean storyToStarted) {
        this.storyToStarted = storyToStarted;
    }
    
}
