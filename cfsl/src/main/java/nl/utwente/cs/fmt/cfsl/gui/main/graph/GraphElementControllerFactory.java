/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.utwente.cs.fmt.cfsl.gui.main.graph;

import nl.utwente.cs.fmt.cfsl.gui.main.graph.edge.abort.ResolveAbortController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.edge.abort.ResumeAbortController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.edge.abort.StartAbortController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.edge.branch.BranchEdgeController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.edge.child.ChildController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.edge.exit.ExitEdgeController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.edge.flow.FlowController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.node.abortstate.AbortStateController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.node.ase.ASEController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.node.branch.BranchNodeController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.node.start.StartController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.node.stop.StopController;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.AbortStateNode;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.AbstractSyntaxElement;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.BranchEdge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.BranchNode;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.ChildEdge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.ExitEdge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.FlowEdge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.GraphElement;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.ResolveAbortEdge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.ResumeAbortEdge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.StartAbortEdge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.StartNode;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.StopNode;

/**
 *
 * @author Richard
 */
public class GraphElementControllerFactory {
    public static GraphElementController build(Class<? extends GraphElement> clazz) {
        if (clazz == AbstractSyntaxElement.class)
            return buildASE();
        if (clazz == StartNode.class)
            return buildStart();
        if (clazz == StopNode.class)
            return buildStop();
        if (clazz == BranchNode.class)
            return buildBranchNode();
        if (clazz == FlowEdge.class)
            return buildFlow();
        if (clazz == ChildEdge.class)
            return buildChild();
        if (clazz == BranchEdge.class)
            return buildBranchEdge();
        if (clazz == StartAbortEdge.class)
            return buildStartAbort();
        if (clazz == ResumeAbortEdge.class)
            return buildResumeAbort();
        if (clazz == ResolveAbortEdge.class)
            return buildResolveAbort();
        if (clazz == ExitEdge.class)
            return buildExitEdge();
        if (clazz == AbortStateNode.class)
            return buildAbortState();
        else
            return null;
    }
    
    public static ASEController buildASE() {
        return new ASEController(new AbstractSyntaxElement());
    }
    
    public static StartController buildStart() {
        return new StartController(new StartNode());
    }
    
    public static StopController buildStop() {
        return new StopController(new StopNode());
    }
    
    public static BranchNodeController buildBranchNode() {
        return new BranchNodeController(new BranchNode());
    }
    
    public static FlowController buildFlow() {
        return new FlowController(new FlowEdge());
    }
    
    public static ChildController buildChild() {
        return new ChildController(new ChildEdge());
    }
    
    public static BranchEdgeController buildBranchEdge() {
        return new BranchEdgeController(new BranchEdge());
    }
    
    public static StartAbortController buildStartAbort() {
        return new StartAbortController(new StartAbortEdge());
    }
    
    public static ResumeAbortController buildResumeAbort() {
        return new ResumeAbortController(new ResumeAbortEdge());
    }
    
    public static ResolveAbortController buildResolveAbort() {
        return new ResolveAbortController(new ResolveAbortEdge());
    }
    
    public static ExitEdgeController buildExitEdge() {
        return new ExitEdgeController(new ExitEdge());
    }
    
    public static AbortStateController buildAbortState() {
        return new AbortStateController(new AbortStateNode());
    }
}
