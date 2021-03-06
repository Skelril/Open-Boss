/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.skelril.openboss;

import org.apache.commons.lang3.Validate;
import org.spongepowered.api.entity.living.Living;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.UUID;

public class Boss<T extends Living, K extends EntityDetail> extends ProcessedComponent<T, K> {

    private WeakReference<T> entity;
    private final UUID entityID;
    private final K detail;

    public Boss(T entity, K detail) {
        this.entity = new WeakReference<>(entity);
        this.entityID = entity.getUniqueId();
        this.detail = detail;
    }

    public Optional<T> getTargetEntity() {
        return Optional.ofNullable(entity.get());
    }

    protected void setTargetEntity(T entity) {
        Validate.isTrue(entity.getUniqueId().equals(entityID));
        this.entity = new WeakReference<>(entity);
    }

    public UUID getEntityID() {
        return entityID;
    }

    public K getDetail() {
        return detail;
    }
}
