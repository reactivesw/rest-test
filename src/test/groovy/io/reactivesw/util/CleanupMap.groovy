package io.reactivesw.util

import sun.java2d.opengl.OGLContext

/**
 * Created by Davis on 17/3/9.
 */
class CleanupMap {
    private Map<String, Integer> objects

    CleanupMap() {
        this.objects = new HashMap<>()
    }

    public void addObject(String id, Integer version) {
        objects.put(id, version)
    }

    public Map getAllObjects() {
        return this.objects
    }
}
