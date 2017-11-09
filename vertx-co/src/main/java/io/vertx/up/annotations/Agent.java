package io.vertx.up.annotations;

import com.vie.cv.em.ServerType;
import io.vertx.up.rs.VertxValues;

import java.lang.annotation.*;

/**
 * For the specification, each agent should only publish one typed server.
 * It's defined by our team, it means that any standard verticle should not
 * contain different typed server instances.
 * But one agent could contain more than one servers distinguished by port.
 * ( Correct ): Agent1 -> HTTP -> HttpServer1, HttpServer2;
 * ( Corrent ): Agent1 -> HTTP -> HttpServer1
 * ( Wrong ): Agent1 -> HTTP -> HttpServer1, RpcServer1;
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Agent {

    interface Key {

        String TYPE = "type";

        String INSTANCES = "instances";

        String GROUP = "group";

        String HA = "ha";
    }

    /**
     * Standard Instance Number
     * Default: 32
     *
     * @return
     */
    int instances() default VertxValues.DEFAULT_INSTANCES;

    /**
     * Isolation Group
     * Default: __VERTX_ZERO__
     *
     * @return
     */
    String group() default VertxValues.DEFAULT_GROUP;

    /**
     * @return
     */
    boolean ha() default VertxValues.DEFAULT_HA;

    /**
     * Default server type: http
     *
     * @return
     */
    ServerType type() default ServerType.HTTP;
}