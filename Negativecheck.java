package sat;
//SIMULATED STATIC CLASS

//FUNCTION: CHECK THAT ENTERED NUMBER IS NEGATIVE. IF NEGATIVE, RETURN -1. ELSE RETURN 1.
//Functions as a checker for the input for is_in during variable updating/instantiation.
public final class Negativecheck {
    private Negativecheck(){}
    public static Integer check(Integer a){
        if(a<0){return -1;}
        else {return 1;}

    }
}
