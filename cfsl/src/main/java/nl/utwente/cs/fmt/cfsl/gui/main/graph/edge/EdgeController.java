/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nl.utwente.cs.fmt.cfsl.gui.main.graph.edge;

import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.QuadCurve;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.GraphController;
import nl.utwente.cs.fmt.cfsl.gui.main.graph.GraphElementController;
import nl.utwente.cs.fmt.cfsl.gui.util.Utils;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.Edge;
import nl.utwente.cs.fmt.cfsl.model.cfslplus.EdgeSide;
import nl.utwente.ewi.caes.tactilefx.control.TactilePane;

/**
 * Controller class for edges.
 * 
 * @author Richard
 * @param <M>
 */
public abstract class EdgeController<M extends Edge> extends GraphElementController<Group, M> {
    @FXML protected QuadCurve curve;
    
    protected final EdgePositionAnchorController startAnchor = new EdgePositionAnchorController(this, EdgeSide.START);
    protected final EdgePositionAnchorController endAnchor = new EdgePositionAnchorController(this, EdgeSide.END);
    protected final EdgeCurveControlAnchorController controlAnchor = new EdgeCurveControlAnchorController(this);
    protected final EdgeCurveControlLineController controlLine = new EdgeCurveControlLineController(controlAnchor);
    
    /**
     * Creates a new EdgeController.
     */
    protected EdgeController(String toolName, M model) {
        super(toolName, model);
        initialize();
    }
    
    /**
     * Creates a new EdgeController.
     * 
     * @param viewName the name of the FXML file that defines the View
     */
    protected EdgeController(String toolName, M model, String viewName) {
        super(toolName, model, viewName);
        initialize();
    }
    
    private void initialize() {
        TactilePane.setDraggable(getView(), false);
        curve.setFill(null);
        
        // Recalculate middle of the curve when curve changes
        curve.startXProperty().addListener(o -> updateMiddle());
        curve.startYProperty().addListener(o -> updateMiddle());
        curve.endXProperty().addListener(o -> updateMiddle());
        curve.endYProperty().addListener(o -> updateMiddle());
        curve.controlXProperty().addListener(o -> updateMiddle());
        curve.controlYProperty().addListener(o -> updateMiddle());
        updateMiddle();
    }
    
    // CANVAS_ELEMENT_CONTROLLER IMPLEMENTATION
    
    @Override
    protected void afterAddedToGraph(GraphController graph) {
        // Add anchors to the canvas
        List canvasViewChildren = graph.getContainer().getChildren();
        canvasViewChildren.add(startAnchor.getView());
        canvasViewChildren.add(endAnchor.getView());
        canvasViewChildren.add(controlAnchor.getView());
        canvasViewChildren.add(controlLine.getView());
        
        // Relocate anchors
        double layoutX = getView().getLayoutX();
        double layoutY = getView().getLayoutY();
        startAnchor.getView().relocate(layoutX + curve.getStartX(), layoutY + curve.getStartY());
        endAnchor.getView().relocate(layoutX + curve.getEndX(), layoutY + curve.getEndY());
        controlAnchor.getView().relocate(layoutX + curve.getControlX(), layoutY + curve.getControlY());
        
        // Relocate entire group to top left of canvas to compensate for bindings
        Bounds b = getView().getBoundsInLocal();
        getView().relocate(b.getMinX(), b.getMinY());
        
        // Bind curve position to position of position anchors
        curve.startXProperty().bind(startAnchor.getView().layoutXProperty());
        curve.startYProperty().bind(startAnchor.getView().layoutYProperty());
        curve.endXProperty().bind(endAnchor.getView().layoutXProperty());
        curve.endYProperty().bind(endAnchor.getView().layoutYProperty());
        
        // Bind curve control to position of control anchors
        curve.controlXProperty().bind(controlAnchor.getView().layoutXProperty());
        curve.controlYProperty().bind(controlAnchor.getView().layoutYProperty());
        
        // Bind control lines between control anchors and curve
        controlLine.getView().startXProperty().bind(curve.controlXProperty());
        controlLine.getView().startYProperty().bind(curve.controlYProperty());
        controlLine.getView().endXProperty().bind(Bindings.createDoubleBinding(() -> { 
            return getMiddle().getX();
        }, middleProperty()));
        controlLine.getView().endYProperty().bind(Bindings.createDoubleBinding(() -> { 
            return getMiddle().getY();
        }, middleProperty()));
        
        // Move control anchor when position anchors move
        startAnchor.getView().layoutXProperty().addListener((observable, oldValue, newValue) -> {
            double deltaX = (double) newValue - (double) oldValue;
            controlAnchor.getView().setLayoutX(controlAnchor.getView().getLayoutX() + deltaX / 2);
        });
        startAnchor.getView().layoutYProperty().addListener((observable, oldValue, newValue) -> {
            double deltaY = (double) newValue - (double) oldValue;
            controlAnchor.getView().setLayoutY(controlAnchor.getView().getLayoutY() + deltaY / 2);
        });
        endAnchor.getView().layoutXProperty().addListener((observable, oldValue, newValue) -> {
            double deltaX = (double) newValue - (double) oldValue;
            controlAnchor.getView().setLayoutX(controlAnchor.getView().getLayoutX() + deltaX / 2);
        });
        endAnchor.getView().layoutYProperty().addListener((observable, oldValue, newValue) -> {
            double deltaY = (double) newValue - (double) oldValue;
            controlAnchor.getView().setLayoutY(controlAnchor.getView().getLayoutY() + deltaY / 2);
        });
        
        // Track position anchors
        graph.getContainer().getActiveNodes().add(startAnchor.getView());
        graph.getContainer().getActiveNodes().add(endAnchor.getView());
        
        // Create inivisible, thicker curve for selection purposes
        QuadCurve invisibleCurve = new QuadCurve();
        invisibleCurve.setOpacity(0);
        invisibleCurve.setStrokeWidth(8);
        invisibleCurve.setFill(null);
        invisibleCurve.setStroke(Color.BLACK);
        invisibleCurve.startXProperty().bind(curve.startXProperty());
        invisibleCurve.startYProperty().bind(curve.startYProperty());
        invisibleCurve.endXProperty().bind(curve.endXProperty());
        invisibleCurve.endYProperty().bind(curve.endYProperty());
        invisibleCurve.controlXProperty().bind(curve.controlXProperty());
        invisibleCurve.controlYProperty().bind(curve.controlYProperty());
        getView().getChildren().add(invisibleCurve);
        invisibleCurve.toBack();
        curve.toBack();
    }
    
    @Override
    protected void beforeRemovedFromGraph(GraphController graph) {
        TactilePane container = graph.getContainer();
        if (startAnchor.getConnector() != null) {
            startAnchor.getConnector().disconnect(startAnchor);
        }
        if (endAnchor.getConnector() != null) {
            endAnchor.getConnector().disconnect(endAnchor);
        }
        container.getActiveNodes().remove(startAnchor.getView());
        container.getActiveNodes().remove(endAnchor.getView());
        container.getChildren().removeAll(startAnchor.getView(), endAnchor.getView(), controlAnchor.getView(), controlLine.getView());
    }
    
    // METHODS
    
    public final void disconnectAnchor(EdgeSide position) {
        if (position == EdgeSide.START) {
            TactilePane.setAnchor(startAnchor.getView(), null);
        } else {
            TactilePane.setAnchor(endAnchor.getView(), null);
        }
    }
    
    // PROPERTIES
    
    /**
     * The coordinates of the point of the Bezier curve for t = 0.5
     */
    private final ObjectProperty<Point2D> middle = new ReadOnlyObjectWrapper<>();

    public Point2D getMiddle() {
        return middle.get();
    }

    public ObjectProperty middleProperty() {
        return middle;
    }

    // HELPER METHODS
    
    private void updateMiddle() {
        middle.set(Utils.eval(curve, 0.5f));
    }
    
    public EdgePositionAnchorController getStartAnchor() {
        return startAnchor;
    }
    
    public EdgePositionAnchorController getEndAnchor() {
        return endAnchor;
    }
    
}
