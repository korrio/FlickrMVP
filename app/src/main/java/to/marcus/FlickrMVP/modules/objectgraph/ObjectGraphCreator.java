package to.marcus.FlickrMVP.modules.objectgraph;

import android.app.Application;
import dagger.ObjectGraph;

/**
 * Created by marcus on 8/14/2015.
 */
public interface ObjectGraphCreator {
    ObjectGraph create(Application application);
}