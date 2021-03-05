## Schema versioning

### Techniques
There exist 5 well-known techniques to address schema versioning
when we talk about an event sourcing schema update.

1. Multiple versions
 * In this technique we store the event version.
 * Event listener support is given to process both of them, even
 if it is temporary.
 * In this technique the event store remains intact as old versions
 are never transformed.  
 * There are no extra write operations needed to convert the store.
 
1. Upcasting
 * In this technique we create a component or process to convert one event
 into the new version/versions of it.
 * Comparing this to the *Multiple versions* technique, in this technique
 the event listener is not aware of the different versions.
 * Event listener only support latest version of the event.
 
1. Lazy transformation
 * It is a technique that also uses the *Upcasting* mindset,
 but besides offering the new version into the application, it also 
 stores it on the event store.
 * It transforms the event on the first read, and then you have the upcasted
 event in your event store.
 
1. In Place Transformation
 * A technique with roots in the SQL world, similar to using the logic of:
 ~~~sql 
    ALTER TABLE ... ;
    UPDATE table_name SET ...;
 ~~~
 * It tries to bridge the gap by using data transformation by means of a query,
 beware most database don't have this capability, but with some
 apps introducing good JSON support, like mysql 
 and postgres now, it is an interesting option!
 * The documents in the database are updated by this
 job: adding, deleting, renaming properties, and transforming
 the values.
 
1. Copy and transformation
 * This technique keeps the old version of the events, and creates a new event store
 and copies and transforms every event to a new store.
 * Maintains the **immutability** of the events! 
 
### Judging the options
We will base our comparison in 4 quality pillars:
* Can it achieve the goal ?
    All five techniques can be implemented to achieve the goal. 
    However, to execute complex store operations such as merging multiple
    streams the technique needs to read from multiple streams.
    The streams could be spread out over different databases and reading 
    them together at the same time in the application is unfeasible.
    
    1. Multiple versions
        * Since this is a runtime technique it violates the independence of streams.
        So it is not in theory functionally complete.
    1. Upcasting
        * Since this is a runtime technique it violates the independence of streams.
        So it is not in theory functionally complete.
    1. Lazy transformation
        * Since this is a runtime technique it violates the independence of streams.
        So it is not in theory functionally complete.
    1. In Place Transformation
        * Since this one is another batch job that does not tie in with reading
        a single stream at a time, it is functionally complete.
    1. Copy and transformation
        * Since this one is another batch job that does not tie in with reading
        a single stream at a time, it is functionally complete.

* Is it Maintainable ?
    This pillar evaluates how easy is to maintain and grow using the
    technique at hand.
    
    1. Multiple versions
      * This forces you to have support in the application code
      for multiple versions, making it a pain to maintain and your codebase 
      will grow and grow with each version.
    1. Upcasting
      * Even tho this is far more maintainable than *Multiple version*
      in the way that your business logic is always one, you still need to have upcasters for each version,
      which means that we will accumulate conversion code as well.
    1. Lazy transformation
      * Very similar to *Upcasting*, even if the argument exists that we will eventually have
      all upcasted events in the event store, we have no way to tell when all event will be converted to the new version.
    1. In Place Transformation
      * Very Maintainable, our code will only have the responsibility of dealing with the new event.
      * After the execution of the data conversion,
        every event is transformed into a new version and thus the
        conversion code is no longer necessary.
    1. Copy and transformation
      * Very Maintainable, equal arguments to *In Place Transformation*
      
* How good is the performance ?
    How does the technique fares when we talk about performance, space + time.
    
    1. Multiple versions
       * Very efficient, since you only need to deal with the event,
       without the extra cost of writing the new data, and besides it, it only deals
       with the events as they are required.
       * The transformations are done in-memory
         as needed, without writing the events back to the store.
    1. Upcasting
       * Very efficient, same arguments as *Multiple Versions*
    1. Lazy transformation
       * It is mid way in this list, since it adds the extra write operations
       that permanently store the changes.
    1. In Place Transformation
       * It is mid way in this list, for the same arguments as *Lazy Transformation*,
       But it is a bit worse in the way it needs to convert inplace every event.
    1. Copy and transformation
        * The worse, with this technique, we need to copy, transform and store again the events in the new Store,
        even if the event is not updatable, it forces you to copy them all.

* How reliable is it ?
    How does the technique affects the auditability/immutability of the events ?
    
    1. Multiple versions
        * Since we keep all original version, this technique has a high reliability, if we wanna query all
        events, that means that we just need to watch the store, we will always have the original one.
    1. Upcasting
        * Same as the *Multiple versions*
    1. Lazy transformation
        * Mid way, depending on where you are with the writes, that can lead you to
        have a lot of events already transformed or not.
    1. In Place Transformation
        * Bad, after you do the transformation, there is no easy way to go back and see the original event,
        meaning, if we mess it up, we mess it up forever!
    1. Copy and transformation
        * Same as *Multiple versions*, since we have multiple stores with all versions, that means that
        we have the ability to audit past events.
