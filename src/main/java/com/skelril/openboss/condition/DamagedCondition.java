/*
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */

package com.skelril.openboss.condition;

import com.skelril.openboss.Boss;
import org.spongepowered.api.entity.living.Living;
import org.spongepowered.api.event.entity.DamageEntityEvent;

import java.util.Optional;

public class DamagedCondition extends Condition {

    private final DamageEntityEvent event;
    private final Optional<Living> optAttacker;

    public DamagedCondition(Boss boss, DamageEntityEvent event) {
        super(boss);
        this.event = event;
        this.optAttacker = event.getCause().first(Living.class);
    }

    public Optional<Living> getAttacker() {
        return optAttacker;
    }

    public DamageEntityEvent getEvent() {
        return event;
    }
}
