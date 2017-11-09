package io.vertx.up.rs;

import com.vie.cv.Values;
import com.vie.cv.em.ServerType;
import com.vie.exception.up.AgentDuplicatedException;
import com.vie.fun.HBool;
import com.vie.util.Instance;
import com.vie.util.log.Annal;
import io.vertx.up.annotations.Agent;
import io.vertx.up.mirror.Anno;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

class AnnoHelper {

    private static final Annal LOGGER = Annal.get(AnnoHelper.class);

    public static ServerType getAgentKey(final Class<?> clazz) {
        return HBool.exec(Anno.isMark(clazz, Agent.class),
                () -> Instance.invoke(getAgentValue(clazz), Agent.Key.TYPE),
                () -> null);
    }

    public static Annotation getAgentValue(final Class<?> clazz) {
        return Anno.get(clazz, Agent.class);
    }

    public static ConcurrentMap<ServerType, Boolean> isAgentDefined(
            final ConcurrentMap<ServerType, List<Class<?>>> agents,
            final Class<?>... exclude) {
        final Set<Class<?>> excludes = new HashSet<>(Arrays.asList(exclude));
        final ConcurrentMap<ServerType, Boolean> defined
                = new ConcurrentHashMap<>();
        for (final ServerType server : agents.keySet()) {
            final List<Class<?>> item = agents.get(server);
            // Filter to result.
            final List<Class<?>> filtered =
                    item.stream()
                            .filter(each -> !excludes.contains(each))
                            .collect(Collectors.toList());
            // > 1 means duplicated defined
            final int size = filtered.size();
            HBool.execUp(1 < size,
                    LOGGER, AgentDuplicatedException.class,
                    AnnoHelper.class, server, size);
            // == 0 means undefined
            // == 1 means correct defined
            defined.put(server, Values.ONE == size);
        }
        return defined;
    }
}