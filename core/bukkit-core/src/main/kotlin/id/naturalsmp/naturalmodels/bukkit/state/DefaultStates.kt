package id.naturalsmp.naturalmodels.bukkit.state

import id.naturalsmp.naturalmodels.api.animation.AnimationIterator
import id.naturalsmp.naturalmodels.api.animation.AnimationModifier
import id.naturalsmp.naturalmodels.api.animation.AnimationState
import id.naturalsmp.naturalmodels.api.entity.BaseEntity

object DefaultStates {

    val IDLE = AnimationState.builder()
        .name("idle")
        .animation("idle")
        .priority(0)
        .predicate { true }
        .modifier(AnimationModifier.builder().type(AnimationIterator.Type.LOOP).build())
        .build()

    val WALK = AnimationState.builder()
        .name("walk")
        .animation("walk")
        .priority(10)
        .predicate { it.onWalk() }
        .modifier(AnimationModifier.builder().type(AnimationIterator.Type.LOOP).build())
        .build()

    val SWIM = AnimationState.builder()
        .name("swim")
        .animation("swim")
        .priority(20)
        .predicate { it.swim() }
        .modifier(AnimationModifier.builder().type(AnimationIterator.Type.LOOP).build())
        .build()

    val FLY = AnimationState.builder()
        .name("fly")
        .animation("fly")
        .priority(30)
        .predicate { it.fly() }
        .modifier(AnimationModifier.builder().type(AnimationIterator.Type.LOOP).build())
        .build()

    val DEATH = AnimationState.builder()
        .name("death")
        .animation("death")
        .priority(100)
        .predicate { it.dead() }
        .modifier(AnimationModifier.builder().type(AnimationIterator.Type.PLAY_ONCE).build())
        .build()
        
    // Add more as needed: jump, fall, etc.
}
