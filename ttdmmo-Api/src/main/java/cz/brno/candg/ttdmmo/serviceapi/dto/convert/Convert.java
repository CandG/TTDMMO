package cz.brno.candg.ttdmmo.serviceapi.dto.convert;

/**
 * Interface for conversion between DTO and entity back and forth. Conversion
 * classes DO NOT verify consistence of data being converted.
 *
 * @author Lastuvka
 */
public interface Convert<T, U> {

    /*
     * @param dto transfer object U
     * @ param em EntityManager passed from the calling object.
     * @return entity T: some of its parameters may be null mostly in case of newly-to-be-created entity 
     */
    public T fromDtoToEntity(U dto);

    /*
     * @param entity T
     * @return transfer object U
     * @throws IllegalArgumentException if entity T has no id.
     */
    public U fromEntityToDto(T entity);
}
