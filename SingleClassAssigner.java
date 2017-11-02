package sat;
//Role: To assign the correct value to this variable that must be placed, given the appearance said variable takes.
public final class SingleClassAssigner {
    private SingleClassAssigner(){}
    public static Integer SingleAssign(Integer A){
        if (A<0){return 0;} //Ie. it has appeared as a negated form in the single clause and must always be 0 to evaluate
        //to true.
        else return 1; //Ie. it has appeared as a normal form in the single clause and must always be 1 to evaluate to true.
    }
}
