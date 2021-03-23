package battleship;

public class Game {
    final String player1 = "Player 1";
    final String player2 = "Player 2";
    public void start() {
        Battlefield battlefield = new Battlefield(player1);
        battlefield.initfield();

        Battlefield battlefield2 = new Battlefield(player2);
        battlefield2.initfield();

        int i=1;
        while (true){
            if(i%2==0){
                System.out.println(battlefield.blindMap());
                System.out.println("---------------------");
                System.out.println(battlefield2.toString());
                if(!battlefield.startShooting()){
                    System.out.println(player2+" won!");
                    break;
                }
            }else {
                System.out.println(battlefield2.blindMap());
                System.out.println("---------------------");
                System.out.println(battlefield.toString());
                if(!battlefield2.startShooting()){
                    System.out.println(player1+" won!");
                    break;
                }
            }
            i++;
        }
    }
}
