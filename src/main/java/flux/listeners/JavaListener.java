package flux.listeners;

import flux.ActionListener;
import flux.KeyFlowContext;

public class JavaListener implements ActionListener {
  @Override
  public Object actionFired(KeyFlowContext keyFlowContext) throws Exception {
    System.out.println("Processing request...");
    Thread.sleep(500);
    System.out.println("Processing done.");
    return null;
  }
}
