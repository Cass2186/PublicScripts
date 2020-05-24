package scripts.wastedbro.api.decision_tree;

import java.util.Stack;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 *
 */
public class DecisionTreeBuilder
{
    private DecisionNode currentNode;

    private Stack<DecisionNode> parentNodeStack = new Stack<>();

    public DecisionTreeBuilder()
    {
        DecisionNode parentNode = createDecisionNode(()-> true);
        parentNode.setInvalidNode((Runnable) () -> {

        });

        parentNodeStack.push(parentNode);
    }

    public DecisionTreeBuilder onValidAction(Runnable runnable)
    {
        if (parentNodeStack.peek().getValidNode() != null)
            throw new RuntimeException("OnValid Node already set for this decision node");

        parentNodeStack.peek().setValidNode(runnable);

        return this;
    }
    public DecisionTreeBuilder onInvalidAction(Runnable runnable)
    {
        if (parentNodeStack.peek().getInvalidNode() != null)
            throw new RuntimeException("OnInvalid Node already set for this decision node");

        parentNodeStack.peek().setInvalidNode(runnable);

        return this;
    }

    public DecisionTreeBuilder onValidDecision(BooleanSupplier decision)
    {
        if (parentNodeStack.peek().getValidNode() != null)
            throw new RuntimeException("OnValid Node already set for this decision node");

        DecisionNode node = createDecisionNode(decision);
        parentNodeStack.peek().setValidNode(node);
        parentNodeStack.push(node);

        return this;
    }

    public DecisionTreeBuilder onInvalidDecision(BooleanSupplier decision)
    {
        if (parentNodeStack.peek().getInvalidNode() != null)
            throw new RuntimeException("OnInvalid Node already set for this decision node");

        DecisionNode node = createDecisionNode(decision);
        parentNodeStack.peek().setInvalidNode(node);
        parentNodeStack.push(node);

        return this;
    }

    public DecisionTreeBuilder onValidSplice(Consumer<DecisionTreeBuilder> decisionTreeBuilderConsumer)
    {
        // In order to splice a new tree into this one, we need to create a fake node that always returns true
        DecisionNode parentNode = createDecisionNode(()-> true);
        parentNode.setInvalidNode((Runnable) () -> {

        });

        parentNodeStack.peek().setValidNode(parentNode);
        parentNodeStack.push(parentNode);

        decisionTreeBuilderConsumer.accept(this);

        parentNodeStack.pop(); // Throw away the fake node

        return this;
    }
    public DecisionTreeBuilder onInvalidSplice(Consumer<DecisionTreeBuilder> decisionTreeBuilderConsumer)
    {
        // In order to splice a new tree into this one, we need to create a fake node that always returns true
        DecisionNode parentNode = createDecisionNode(()-> true);
        parentNode.setInvalidNode((Runnable) () -> {

        });

        parentNodeStack.peek().setInvalidNode(parentNode);
        parentNodeStack.push(parentNode);

        decisionTreeBuilderConsumer.accept(this);

        parentNodeStack.pop(); // Throw away the fake node

        return this;
    }

    public DecisionTreeBuilder end()
    {
        currentNode = parentNodeStack.pop();
        return this;
    }

    public DecisionTree build()
    {
        if(currentNode == null)
            throw new RuntimeException("There are no nodes to build");

        if(parentNodeStack.size() > 1)
            System.out.println("Warning: Decision tree is building from a child node");

        return new DecisionTree(currentNode);
    }

    private DecisionNode createDecisionNode(BooleanSupplier isValidSupplier)
    {
        return new DecisionNode()
        {
            @Override
            public boolean isValid()
            {
                return isValidSupplier.getAsBoolean();
            }
        };
    }
}
