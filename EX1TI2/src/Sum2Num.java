import java.util.*;


 class Sum2Num {
	 public static Scanner sc= new Scanner (System.in);
	 
	 public static void main(String [] args) {
		 //declaracao de variaveis
		 int num1, num2, soma;
		 //leitura
		 System.out.print("Digite um numero: ");
		 num1= sc.nextInt();
		 System.out.print("Digite proximo numero: ");
		 num2 = sc.nextInt();
		 // somar
		 soma =num1+num2;
		 //print
		 System.out.print("Soma:" + soma);
	 }
 
 }
