public class Main {

    public static void main(String[] args) {
        try {
            ChainCall chainCall = new ChainCall(args[0]);
            chainCall.reduceChainCall();
            System.out.println(chainCall.getReducedChainStr());
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
    }
}
