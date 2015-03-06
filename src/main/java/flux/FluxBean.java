package flux;

import flux.listeners.JavaListener;
import fluximpl.com.google.common.base.Strings;

import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import java.io.Serializable;
import java.util.*;

@ManagedBean(name = "engine")
@ApplicationScoped
public class FluxBean implements Serializable {

  private Engine engine;

  private List<FluxWorkflow> workflows = new ArrayList<FluxWorkflow>();

  public FluxBean() throws EngineException {
    System.out.println("Initializing Flux engine...");
    Configuration config = new Configuration();
    config.setDatabaseType(DatabaseType.MYSQL);
    config.setServer(true);
    config.setUrl("jdbc:mysql://localhost:3306/fluxjsf");
    config.setDriver("com.mysql.jdbc.Driver");
    config.setJdbcUsername("flux");
    config.setJdbcPassword("flux");
    config.setInternalLoggerFileDirectory("./logs");
    this.engine = Factory.makeInstance().makeEngine(config);
    engine.clear();
    initializeRepositoryWorkflows();
    engine.start();
    System.out.println("Flux engine started.");
  }

  public List<FluxWorkflow> getWorkflows() {
    return workflows;
  }

  public static class FluxWorkflow {
    String name;

    public FluxWorkflow(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }

  private void initializeRepositoryWorkflows() throws EngineException {
    FlowChart workflow1 = EngineHelper.makeFlowChart("Order Processing");
    DelayTrigger delay = workflow1.makeDelayTrigger("delay");

    JavaAction ja = workflow1.makeJavaAction("listener");
    ja.setListener(JavaListener.class);

    delay.addFlow(ja);
    engine.getRepositoryAdministrator().put(workflow1, true);
    workflows.add(new FluxWorkflow(workflow1.getName()));

    FlowChart workflow2 = EngineHelper.makeFlowChart("Generate Reports");
    DelayTrigger delay2 = workflow2.makeDelayTrigger("delay");

    JavaAction ja2 = workflow2.makeJavaAction("listener");
    ja2.setListener(JavaListener.class);

    delay2.addFlow(ja2);
    engine.getRepositoryAdministrator().put(workflow2, true);
    workflows.add(new FluxWorkflow(workflow2.getName()));

  }

  public void schedule(String templateName) throws EngineException {
    if (Strings.isNullOrEmpty(templateName)) {
      return;
    }
    Map<String, String> variables = new HashMap<String, String>();
    UUID uuid = UUID.randomUUID();
    String workflowName = templateName + "/" + uuid.toString();
    String scheduled = engine.putFromRepository(templateName, workflowName, variables, true);
    System.out.println("Starting workflow: " + scheduled);
  }

  @PreDestroy
  public void dispose() {
    System.out.println("Disposing Flux engine..");
    try {
      engine.dispose();
      System.out.println("Flux engine disposed.");
    } catch (EngineException e) {
      System.out.println("Error disposing engine. Reason: " + e.getMessage());
    }
  }

}
