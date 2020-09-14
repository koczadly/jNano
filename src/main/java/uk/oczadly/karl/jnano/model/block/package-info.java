/*
 * Copyright (c) 2020 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

/**
 * This package provides a range of different implementations for representing a transactional block.
 *
 * <p>If implementing your own block type, you should extend the {@link uk.oczadly.karl.jnano.model.block.Block}
 * class, and implement any associated interfaces provided in the {@link uk.oczadly.karl.jnano.model.block.interfaces}
 * package.</p>
 *
 * <p>The following block implementations are provided:</p>
 * <ul>
 *     <li>{@code open} - {@link uk.oczadly.karl.jnano.model.block.OpenBlock}</li>
 *     <li>{@code send} - {@link uk.oczadly.karl.jnano.model.block.SendBlock}</li>
 *     <li>{@code receive} - {@link uk.oczadly.karl.jnano.model.block.ReceiveBlock}</li>
 *     <li>{@code change} - {@link uk.oczadly.karl.jnano.model.block.ChangeBlock}</li>
 *     <li>{@code state} - {@link uk.oczadly.karl.jnano.model.block.StateBlock}</li>
 * </ul>
 *
 * @author Karl Oczadly
 */
package uk.oczadly.karl.jnano.model.block;