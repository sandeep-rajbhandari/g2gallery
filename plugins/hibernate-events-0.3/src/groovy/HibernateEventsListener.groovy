import org.hibernate.event.PreLoadEventListener
import org.hibernate.event.PostLoadEventListener
import org.hibernate.event.PostInsertEventListener
import org.hibernate.event.PostUpdateEventListener
import org.hibernate.event.PostDeleteEventListener
import org.hibernate.event.PreLoadEvent
import org.hibernate.event.PostLoadEvent
import org.hibernate.event.PostInsertEvent
import org.hibernate.event.PostUpdateEvent
import org.hibernate.event.PostDeleteEvent
import org.hibernate.event.PreUpdateEventListener
import org.hibernate.event.PreInsertEventListener
import org.hibernate.event.PreInsertEvent
import org.hibernate.event.PreUpdateEvent

class HibernateEventsListener implements PreLoadEventListener, PostLoadEventListener,
                                         PostInsertEventListener, PreInsertEventListener,
                                         PostUpdateEventListener, PreUpdateEventListener,
                                         PostDeleteEventListener {

    public void onPostDelete(PostDeleteEvent event) {
        handleEvent("afterDelete", event.entity)
    }

    public void onPostUpdate(PostUpdateEvent event) {
        handleEvent("afterUpdate", event.entity)
        handleEvent("afterSave", event.entity)
    }

    public void onPostInsert(PostInsertEvent event) {
        handleEvent("afterInsert", event.entity)
        handleEvent("afterSave", event.entity)
    }

    public void onPostLoad(PostLoadEvent event) {
        handleEvent("afterLoad", event.entity)
    }

    public void onPreLoad(PreLoadEvent event) {
        handleEvent("beforeLoad", event.entity)
    }

    public boolean onPreInsert(PreInsertEvent event) {
        handleEvent("beforeSave", event.entity)
        return false;
    }

    public boolean onPreUpdate(PreUpdateEvent event) {
        handleEvent("beforeSave", event.entity)
        return false;
    }

    def handleEvent(eventHandler, entity) {
        if(entity.metaClass.hasProperty(entity, eventHandler)) {
            entity."${eventHandler}"()
        }
    }
}