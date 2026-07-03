package org.vmstudio.visor.common.session;

/**
 * {@code offhandSlot == -1} means "unset"
 */
public record VrPrefs(float rotationY, int offhandSlot){
    public static final VrPrefs DEFAULT = new VrPrefs(0f, -1);
}
