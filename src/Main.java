public class Main {

    public static void main(String[] args) {
        if (args.length != 0) {
            try {
                ChainCall chainCall = new ChainCall(args[0]);
                chainCall.reduceChainCall();
                System.out.println(chainCall.getReducedChainStr());
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
