package be.xtrit.cassandra.mapper

import java.lang.annotation.Annotation
import java.lang.reflect.Field

import be.xtrit.cassandra.Column
import com.datastax.driver.core.Row
import org.springframework.stereotype.Component

class ClassMapping[T](clazz: Class[T]) {
    private val columnToFieldMap = getColumnToFieldMapForClass(clazz)

    private def getColumnToFieldMapForClass(clazz: Class[T]) =
        addEntriesForAnnotatedFields(clazz.getDeclaredFields, Map[String, Field]())

    private def addEntriesForAnnotatedFields(fields:Array[Field], mapUntilNow: Map[String,Field]): Map[String, Field] = {
        if (fields.isEmpty)
            mapUntilNow
        else {
            val field = fields.head
            val mapAfterProcessingAnnotations = addEntryForTheColumnAnnotation(field.getDeclaredAnnotations, field, mapUntilNow)
            addEntriesForAnnotatedFields(fields.tail, mapAfterProcessingAnnotations)
        }
    }

    private def addEntryForTheColumnAnnotation(annotations: Array[Annotation],
                                               field: Field,
                                               mapUntilNow: Map[String, Field] ): Map[String, Field] = {
        if (annotations.isEmpty) {
            mapUntilNow
        } else {
            val annotation = annotations.head
            annotation match {
                case column: Column =>
                    mapUntilNow + (column.value() -> field)
                case _ =>
                    addEntryForTheColumnAnnotation(annotations.tail, field, mapUntilNow)
            }
        }
    }

    def mapRow(row: Row): T = {
        val cstr = clazz.getConstructor()
        val instance = cstr.newInstance()

        setFields(instance, row, 0, row.getColumnDefinitions.size())
    }

    private def setFields(instance: T, row:Row, index: Int, nbOfFields: Int): T = {
        if(index == nbOfFields) {
            instance
        } else {
            val columnName = row.getColumnDefinitions.getName(index)
            val instanceWithFieldSet = setField(instance, columnName, row.getObject(index))
            setFields(instanceWithFieldSet, row, index + 1, nbOfFields)
        }
    }

    private def setField(instance: T, columnName:String, anyRef: AnyRef): T = {
        if(columnToFieldMap.contains(columnName)) {
            val field = columnToFieldMap(columnName)
            field.setAccessible(true)
            field.set(instance, anyRef)
        }

        instance
    }
}

@Component
class ClassMappingFactory {
    def create[T](clazz: Class[T]) = new ClassMapping[T](clazz)
}