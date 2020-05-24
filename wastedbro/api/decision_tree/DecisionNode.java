package scripts.wastedbro.api.decision_tree;

import java.util.function.BooleanSupplier;

/**
 * @author Wastedbro
 */
public abstract class DecisionNode
{
    private Object valid;
    private Object invalid;

    public abstract boolean isValid();

    public Object getValidNode() {
        return valid;
    }

    public void setValidNode(Object valid) {
        this.valid = valid;
    }

    public Object getInvalidNode() {
        return invalid;
    }

    public void setInvalidNode(Object invalid) {
        this.invalid = invalid;
    }



    public void print(String prefix, boolean isOnTrue)
    {
        System.out.println(prefix + ("|---- " + (isOnTrue ? "[TRUE]" : "[FALSE]") + " IF " + this.getClass().getSimpleName()));


        if(valid instanceof DecisionNode)
            ((DecisionNode)valid).print(prefix + "      ", true);
        else
            System.out.println(prefix + "      |---- [TRUE] " + valid.getClass().getSimpleName());

        if(invalid instanceof DecisionNode)
            ((DecisionNode)invalid).print(prefix + "      ", false);
        else
            System.out.println(prefix + "      |---- [FALSE] " + invalid.getClass().getSimpleName());
    }
}