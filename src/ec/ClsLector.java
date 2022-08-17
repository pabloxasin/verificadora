package ec;
import java.util.ArrayList;
import javax.smartcardio.*;
import java.util.ArrayList;
import javax.smartcardio.*;
import javax.smartcardio.Card;
import javax.smartcardio.CardChannel;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.CardTerminals;
import javax.smartcardio.CommandAPDU;
import javax.smartcardio.ResponseAPDU;
import javax.smartcardio.TerminalFactory;

public class ClsLector {
	private Card card;
	private CardTerminal terminal;
    private CardChannel channel;
    
    public String getSerial() throws CardException {
    	String a = "";
    	        TerminalFactory factory = TerminalFactory.getDefault();
    	        CardTerminals terminals = factory.terminals();
    	        if (terminals.list(CardTerminals.State.CARD_INSERTION).isEmpty()) 
    	        	{
    	        	   	return a;
    	        	}
    	        else 	
    	        {    
    	            terminal = terminals.list(CardTerminals.State.CARD_INSERTION).get(0);
    	            card = terminal.connect("*");
    	            channel = card.getBasicChannel();
    	            byte[] getCplc = {(byte) 0x80, (byte) 0xCA, (byte) 0x9F, (byte) 0x7F, 0x00};
    	            ResponseAPDU answer = channel.transmit(new CommandAPDU(getCplc));
    	            byte r[] = answer.getData();
    	            for (int i = 13; i < 21; i++) 
    	            {
    	                a = a + String.format("%02X", r[i]);
    	            // System.out.println("dato "+i+" "+a);
    	            }

    	            return a;
    	        }
    	    }

}
