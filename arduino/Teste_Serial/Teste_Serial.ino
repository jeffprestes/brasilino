String frase = "Hello World";
String str;
char c;
char matriz[20];
int x=0;

void setup() 	{
	Serial.begin(9600);
}

void loop() {
	
	if(Serial.available()) 	{
		Serial.println("chegou algo");
		do{
			c=Serial.read();
			matriz[x]=c;
			//Serial.print(matriz[x],DEC);
			x++;
			delay(1);      //Delay para o Arduino não perder o dado da Serial
		}	while(Serial.available());

		matriz[x-1]='\0';
		Serial.println(matriz);
		str=matriz;

		if (str == frase)		{
			Serial.println("OK"); 
		} else	{
			Serial.println("Erro");
		}
	}	else	{
		Serial.println("Nao ha dados...");
		delay(4000);
	}
}