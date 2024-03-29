/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.utwente.cs.fmt.cfsl.gui.main.graph.edge;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import nl.utwente.cs.fmt.cfsl.gui.Controller;
import nl.utwente.cs.fmt.cfsl.gui.main.MainController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.node.EdgeConnectorController;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.EdgeSide;
import nl.utwente.ewi.caes.tactilefx.control.Anchor;
import nl.utwente.ewi.caes.tactilefx.control.TactilePane;

/**
 * The controller for an edge anchor that manipulates either the start or end
 * position of an edge. When dropped on an EdgeConnector, it may connect its
 * edge with the EdgeConnector's Node.
 * 
 * @author Richard
 */
public class EdgePositionAnchorController extends EdgeAnchorController {
    private final EdgeSide position;
    
    /**
     * Creates a new EdgeCurvePositionAnchorController that manipulates the given
     * edge.
     * 
     * @param edge the edge this anchor manipulates. May not be null.
     * @param position the position of the edge that this anchor manipulates
     */
    public EdgePositionAnchorController(EdgeController edge, EdgeSide position) {
        super(edge);
        
        edge.selectedProperty().addListener(o -> { 
            setVisible(edge.isSelected() || getConnector() == null);
        });
        connectorProperty().addListener(o -> { 
            setVisible(edge.isSelected() || getConnector() == null);
        });
        
        this.position = position;
    }
    
    // PROPERTIES
    
    /**
     * The controller for the EdgeConnector that this position anchor is 
     * connected to, if any.
     */
    private final SimpleObjectProperty<EdgeConnectorController> connector = new SimpleObjectProperty<>();

    public EdgeConnectorController getConnector() {
        return connector.get();
    }
    
    public void setConnector(EdgeConnectorController value) {
        connector.set(value);
    }

    public ObjectProperty<EdgeConnectorController> connectorProperty() {
        return connector;
    }
    
    public EdgeSide getPosition() {
        return this.position;
    }
    
    // EVENT HANDLING
    
    @FXML
    void mouseEntered(MouseEvent event) {
        setVisible(true);
    }

    @FXML
    void mouseExited(MouseEvent event) {
        if (getConnector() != null && !getEdge().isSelected()) {
            setVisible(false);
        }
    }
    
    @FXML
    void mousePressed(MouseEvent event) {
        MainController.getInstance().getGraph().showAllEdgeConnectors(true);
        if (getConnector() != null) {
            getConnector().disconnect(this);
            this.connector.set(null);
        }
    }
    
    @FXML
    void mouseReleased(MouseEvent event) {
        EdgeConnectorController rejectingConnector = null;
        for (Node node: TactilePane.getNodesInProximity(getView())) {
            Controller controller = Controller.getController(node);
            if (controller != null && controller instanceof EdgeConnectorController) {
                EdgeConnectorController connector = (EdgeConnectorController) controller;
                if (connector.connect(this)) {
                    TactilePane.setAnchor(getView(), new Anchor(node, 5, 5, Pos.TOP_LEFT, true));
                    this.connector.set((EdgeConnectorController) controller);
                    setVisible(false);
                    rejectingConnector = null;
                    break;
                } else {
                    rejectingConnector = connector;
                }
            }
        }
        if (rejectingConnector != null) {
            TactilePane.moveAwayFrom(getView(), rejectingConnector.getNode().getView());
        }
        MainController.getInstance().getGraph().showAllEdgeConnectors(false);
    }
}
