/*
 * Copyright (c) 2014 Wyatt Childers.
 *
 * This file is part of OSBL.
 *
 * OSBL is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * OSBL is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with OSBL.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.skelril.OSBL.bukkit.listener;

import com.skelril.OSBL.bukkit.BukkitBossDeclaration;
import com.skelril.OSBL.bukkit.entity.BukkitEntity;
import com.skelril.OSBL.bukkit.util.BukkitAttackDamage;
import com.skelril.OSBL.bukkit.util.BukkitDamageSource;
import com.skelril.OSBL.entity.Boss;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;

public class DefaultBukkitListener implements BukkitListener {

    private BukkitBossDeclaration declaration;

    public DefaultBukkitListener(BukkitBossDeclaration declaration) {
        assert declaration != null;
        this.declaration = declaration;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        Entity hurt = event.getEntity();
        double damage = event.getDamage();

        Boss boss = declaration.getBound(new BukkitEntity<>(hurt));

        // A boss of this type was harmed
        if (boss != null) {
            BukkitDamageSource attacker;
            EntityDamageEvent.DamageCause cause = event.getCause();
            if (event instanceof EntityDamageByBlockEvent) {
                Block damager = ((EntityDamageByBlockEvent) event).getDamager();
                attacker = new BukkitDamageSource(damager, cause);
            } else if (event instanceof EntityDamageByEntityEvent) {
                Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
                attacker = new BukkitDamageSource(damager, cause);
            } else {
                attacker = new BukkitDamageSource(cause);
            }

            declaration.damaged(boss, attacker, damage);
        } else if (event instanceof EntityDamageByEntityEvent) {
            Entity damager = ((EntityDamageByEntityEvent) event).getDamager();
            boss = declaration.getBound(new BukkitEntity<>(damager));

            // A boss of this type attacked
            if (boss != null && damage > 0) {
                declaration.damage(boss, new BukkitEntity<>(hurt), new BukkitAttackDamage((EntityDamageByEntityEvent) event));
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEnityDeath(EntityDeathEvent event) {
        Entity dead = event.getEntity();

        Boss boss = declaration.getBound(new BukkitEntity<>(dead));
        if (boss != null) {
            declaration.unbind(boss);
        }
    }
}