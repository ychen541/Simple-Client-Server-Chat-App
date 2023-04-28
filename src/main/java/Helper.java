import java.util.ArrayList;
import java.io.Serializable;

public class Helper implements Serializable{
    private static final long serialVersionUID = 1L;

    ArrayList<Integer>clientListhelper;
    int size;

    Helper(){
        clientListhelper = new ArrayList<Integer>();
        size =0;
    }

    void setClientListhelper(ArrayList<Integer>list){
        clientListhelper = list;
    }

    ArrayList<Integer> getClientListhelper(){
        return clientListhelper;
    }

    int getSize(){
        return  clientListhelper.size();
    }
}
