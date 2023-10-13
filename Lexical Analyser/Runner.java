public class Runner {
    public static void main(String[] args) {
        try {
            System.out.println(LexicalAnalyser.analyse( "3 - 51414 - 143 30010 / 3130"));
        }
        catch (NumberException e) {

        }
        catch (ExpressionException e) {
            
        }
    }
}
