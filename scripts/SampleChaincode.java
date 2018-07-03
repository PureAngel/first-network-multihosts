import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hyperledger.java.shim.ChaincodeBase;
import org.hyperledger.java.shim.ChaincodeStub;

public class SimpleChaincode extends ChaincodeBase {
  private static Log log = LogFactory.getLog(SimpleChaincode.class);

  @Override
  public String run(ChaincodeStub stub, String function, String[] args) {
    switch(function) {
      case "init":
      init(stub, function, args);
      break;
      case "transfer":
      return transfer(stub, args);
      case "query":
      return queryAsset(stub, args);
    }
    return null;
  }

  private String transfer(ChaincodeStub stub, String[] args) {
    if (args.length != 3) {
      System.out.pringln("Incorrect number of arguments:" + args.length);
      return "{\"Error\":\"Incorrect number of arguments. Expecting 3: from, to, amouont\"}";
    }
    String fromName = args[0];
    String toName = args[1];
    String am = args[2];
    String toAm = stub.getState(toName);
    String fromAm = stub.getState(fromName);

    int valFrom = Integer.parseInt(fromAm) - Integer.parseInt(am);
    int valTo = Integer.parseInt(toAm) + Integer.parseInt(am);
    stub.putState(fromName, String.valueOf(valFrom));
    stub.putState(toName, String.valueOf(valTo));
    System.out.println("Transfer complete");
    return null;
  }

  public String init(ChaincodeStub stub, String function, String[] args) {
    if(args.length != 4) {
      return "{\"Error\":\"Incorrect number of arguments. Expecting 4\"}";
    }
    int valA = Integer.parseInt(args[1]);
    int valB = Integer.parseInt(args[3]);
    stub.putState(args[0], String.valueOf(valA));
    stub.putState(args[2], String.valueOf(valB));
    return null;
  }

  public String queryAsset(ChaincodeStub stub, String[] args) {
    return stub.getState(args[0]);
  }

  @Override
  public String query(ChaincodeStub stub, String function, String[] args) {
    return null;
  }

  @Override
  public String getChaincodeID() {
    return "simple";
  }

  public static void main(String[] args) throws Exception {
    new SimpleChaincode().start(args);
  }

}
