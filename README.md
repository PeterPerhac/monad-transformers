# Monad Transformers

Under the guidance of Peter Perháč, we will look into monad transformers (MT); what they are, why and how to use them. Since Monad type class is not part of the Scala standard library, we will be leaning on abstractions and using data types from the Typelevel Cats library. The aim of this session is to get everyone acquainted with the idea of monad transformers (and with Cats).

We will explore different ways they can be used to make your code neater and help with everyday tasks. Look at using MTs in for-comprehensions and covering a couple of lifting techniques to achieve this. We'll also explore using MTs outside of for-comprehensions, getting familiar with methods like `subflatMap`, `semiflatMap`, etc.

Understanding monads and type classes is not a prerequisite for this session (though having a rough idea what they are will help).


-----

## Monads don't compose

Monad `F` knows how to flatten `F[F[A]]` into a `F[A]`.
Monad `G` knows how to flatten `G[G[A]]` into a `G[A]`.

Having a monad `F` and monad `G`, can we compose them?

No, not generically. Monads don't compose.

By composing monad `F` and monad `G` we won't magically get a monad that knows how to flatten `F[G[F[G[A]]]]` into an `F[G[A]]` because there's always an `F` or a `G` in the way. Monad `F` can't know what `G` will be (and vice-versa), so monads can't be composed _generically_.

However, in some special cases, certain monads _can_ be composed in a specific way. This is where monad transformers come into play. We can't have MTs for all monads, but for some it's possible: e.g. `OptionT`, `EitherT`, `ReaderT`, `WriterT`.

Not all monads have a respective monad transformer.

In some cases, though it is possible to write specialised monads (yes, a monad transformer is in itself a monad) that offer the effects of both the base monad and the wrapping monad.

For example, the `OptionT[F[_], A]` monad transformer must implement all the functionality of the Option monad, _and_ delegate to the underlying base monad (`F`).

------

![cats-typeclass-hierarchy](res/cats-typeclass-hierarchy.png)
