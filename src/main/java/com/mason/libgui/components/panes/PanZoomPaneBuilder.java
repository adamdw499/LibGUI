package com.mason.libgui.components.panes;

import com.mason.libgui.components.behaviour.camera.*;
import com.mason.libgui.core.component.HitboxRect;
import com.mason.libgui.core.componentManagement.SimpleUIComponentContainer;
import com.mason.libgui.core.componentManagement.UIComponentManager;
import com.mason.libgui.utils.structures.RectQuery;

import java.awt.*;
import java.util.function.Consumer;

public class PanZoomPaneBuilder<T extends PanZoomPane>{


    private final PaneConstructor<T> constructor;


    public PanZoomPaneBuilder(PaneConstructor<T> constructor){
        this.constructor = constructor;
    }


    public T build(HitboxRect boundary, RectQuery initialView, Zoom zoom){
        BehaviourFactory behaviourFactory = buildStandardBehaviourFactory(boundary, initialView, zoom);
        return buildUsingBehaviourFactory(boundary, behaviourFactory);
    }

    private BehaviourFactory buildStandardBehaviourFactory(HitboxRect boundary, RectQuery initialView, Zoom zoom){
        return (renderReference, viewportCapturer) -> {
            return PanZoomBehaviour.buildBehaviour(renderReference, boundary, initialView, zoom, viewportCapturer);
        };
    }

    private T buildUsingBehaviourFactory(HitboxRect boundary, BehaviourFactory behaviourFactory){
        RenderReference renderReference = new RenderReference();
        ViewportMouseInputCapturer viewportCapturer = new ViewportMouseInputCapturer();
        PanZoomBehaviour behaviour = behaviourFactory.build(renderReference, viewportCapturer);
        T pane = buildUnreferencedPaneFromBehaviour(boundary, behaviour, viewportCapturer);
        renderReference.setPane(pane);
        return pane;
    }

    private T buildUnreferencedPaneFromBehaviour(HitboxRect boundary, PanZoomBehaviour behaviour, ViewportMouseInputCapturer viewportCapturer){
        UIComponentManager componentManager = buildViewportComponentManager(behaviour, viewportCapturer);
        PanZoomPaneSkeleton skeleton = buildSkeleton(boundary, behaviour, componentManager);
        return constructor.constructUnwiredPane(skeleton);
    }

    private static UIComponentManager buildViewportComponentManager(PanZoomBehaviour behaviour, ViewportMouseInputCapturer viewportCapturer){
        ViewportInputDistributor inputDistributor = new ViewportInputDistributor(behaviour, viewportCapturer);
        return UIComponentManager.buildUIComponentManager(new SimpleUIComponentContainer(), inputDistributor);
    }

    private static PanZoomPaneSkeleton buildSkeleton(HitboxRect boundary, PanZoomBehaviour behaviour, UIComponentManager componentManager){
        PanZoomPaneSkeleton skeleton = new PanZoomPaneSkeleton();
        skeleton.setBehaviour(behaviour);
        skeleton.setBoundary(boundary);
        skeleton.setComponentManager(componentManager);
        return skeleton;
    }


    public T buildFullyZoomedOutPane(HitboxRect boundary){
        BehaviourFactory behaviourFactory = buildFullyZoomedOutBehaviourFactory(boundary);
        return buildUsingBehaviourFactory(boundary, behaviourFactory);
    }

    private BehaviourFactory buildFullyZoomedOutBehaviourFactory(HitboxRect boundary){
        return (renderReference, viewportCapturer) -> {
            return PanZoomBehaviour.buildFullyZoomedOutDefaultBehaviour(renderReference, boundary, viewportCapturer);
        };
    }


    private static final class RenderReference implements Consumer<Graphics2D>{

        PanZoomPane pane;

        void setPane(PanZoomPane pane){
            this.pane = pane;
        }

        @Override
        public void accept(Graphics2D g){
            pane.renderRawPane(g);
        }

    }

    public interface PaneConstructor<T>{

        T constructUnwiredPane(PanZoomPaneSkeleton skeleton);

    }

    private interface BehaviourFactory{

        PanZoomBehaviour build(RenderReference renderReference, ViewportMouseInputCapturer viewportCapturer);

    }

}
