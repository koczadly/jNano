 /*
  * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
  * Licensed under the MIT License
  */

 package uk.oczadly.karl.jnano.model.block.interfaces;

 /**
  * @author Karl Oczadly
  */

 public interface IBlockSelfVerifiable {
    
     /**
      * Tests whether the signature is valid and was signed by the correct account.
      *
      * @return true if the signature is correct, false if not <em>or</em> if the {@code signature} is currently null
      */
     boolean verifySignature();
 
 }
