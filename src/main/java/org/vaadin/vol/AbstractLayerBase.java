package org.vaadin.vol;

import java.lang.reflect.Method;
import java.util.Map;


import com.vaadin.tools.ReflectTools;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Component;

/*	
 * Layer base class to handle the basic layer events
 * 
 *  The move events currently not handled. They should handled in map.
 *  I don't implement listeners for add or remove layer because I found no
 *  practicle use case :-D
 */
abstract public class AbstractLayerBase extends AbstractComponent {
	
	/*
	 * add a listener to layers 'loadstart' event
	 */
    public void addListener(LoadStartListener listener) {
        addListener(LoadStartListener.EVENT_ID, LoadStartEvent.class,
                listener, LoadStartListener.method);
    }

    public void removeListener(LoadStartListener listener) {
        removeListener(LoadStartListener.EVENT_ID,
                LoadStartEvent.class, listener);
    }

	/*
	 * add a listener to layers 'loadend' event
	 */
    public void addListener(LoadEndListener listener) {
        addListener(LoadEndListener.EVENT_ID, LoadEndEvent.class,
                listener, LoadEndListener.method);
    }

    public void removeListener(LoadEndListener listener) {
        removeListener(LoadEndListener.EVENT_ID,
                LoadEndEvent.class, listener);
    }

    
	/*
	 * add a listener to layers 'visabilitychanged' event
	 */
    public void addListener(VisibilityChangedListener listener) {
        addListener(VisibilityChangedListener.EVENT_ID, VisibilityChangedEvent.class,
                listener, VisibilityChangedListener.method);
    }

    public void removeListener(VisibilityChangedListener listener) {
        removeListener(VisibilityChangedListener.EVENT_ID,
        		VisibilityChangedEvent.class, listener);
    }
    
    
    @SuppressWarnings("unchecked")
    @Override
    public void changeVariables(Object source, Map<String, Object> variables) {
        super.changeVariables(source, variables);
        if (variables.get(LoadStartListener.EVENT_ID)!=null) {
        	LoadStartEvent event=new LoadStartEvent(this,
        				(String)variables.get(LNAME));
        	fireEvent(event);
        }
        else if (variables.get(LoadEndListener.EVENT_ID)!=null) {
        	LoadEndEvent event=new LoadEndEvent(this,
        				(String)variables.get(LNAME));
        	fireEvent(event);        	
        }
        else if (variables.get(VisibilityChangedListener.EVENT_ID)!=null) {
        	VisibilityChangedEvent event=new VisibilityChangedEvent(this,
        				(String)variables.get(LNAME),
        				(Boolean)variables.get(LVIS));
        	fireEvent(event);        	        	
        }
    }
    
	public interface LoadStartListener {
        public final String EVENT_ID = "llstart";

        public final Method method = ReflectTools.findMethod(
                LoadStartListener.class, "loadStart",
                LoadStartEvent.class);

        public void loadStart(LoadStartEvent event);
    }

	public interface LoadEndListener {
        public final String EVENT_ID = "llend";

        public final Method method = ReflectTools.findMethod(
                LoadEndListener.class, "loadEnd",
                LoadEndEvent.class);

        public void loadEnd(LoadEndEvent event);
    }

	public interface VisibilityChangedListener {
        public final String EVENT_ID = "lvis";

        public final Method method = ReflectTools.findMethod(
                VisibilityChangedListener.class, "visibilityChanged",
                VisibilityChangedEvent.class);

        public void visibilityChanged(VisibilityChangedEvent event);
    }
	
	
    public class LayerBaseEvent extends Event {
    	private String layerName;
    	
		public LayerBaseEvent(Component source, String layerName) {
            super(source);
            this.layerName=layerName;
        }

		public String getLayerName() {
			return layerName;
		}
    }
	
    public class LoadStartEvent extends LayerBaseEvent {
		public LoadStartEvent(Component source, String layerName) {
            super(source,layerName);
        }
    }

    public class LoadEndEvent extends LayerBaseEvent {
		public LoadEndEvent(Component source, String layerName) {
            super(source,layerName);
        }
    }

    public class VisibilityChangedEvent extends LayerBaseEvent {
    	private boolean visible;
		public VisibilityChangedEvent(Component source, String layerName,Boolean visible) {
            super(source,layerName);
            
            // false only in case visible variable not set
            this.visible=visible!=null?visible.booleanValue():false;
        }
		
		public boolean isVisible() {
			return this.visible;
		}
    }
    
    private final static String LNAME="lname";
    private final static String LVIS="lisvis";
}
