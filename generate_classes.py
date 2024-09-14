import json
import itertools
import re

fileName = '/home/terabyte/dev/perso/factory-planner/factory-planner/satisfactory-loader/src/main/resources/data.json'
fileDesc = open(fileName, 'r', encoding='utf-16')
jsonString = fileDesc.read()
data = json.loads(jsonString)


def is_nullable(field, elem, whole_data):
    return field not in elem


def is_object(field, elem, whole_data):
    return isinstance(elem.get(field), dict)


def is_array(field, elem, whole_data):
    return isinstance(elem.get(field), list)


def is_array_of_object(field, elem, whole_data):
    return is_array(field,  elem, whole_data) and len(elem.get(field)) > 0 and isinstance(elem.get(field)[0], dict)


def is_number(field, elem, whole_data):
    return field in elem and  isinstance(elem.get(field), str) and re.search(r'^[0-9]+$', elem.get(field)) is not None

def is_float(field, elem, whole_data):
    return field in elem and isinstance(elem.get(field), str) and re.search(r'^[0-9]+\.[0-9]+$', elem.get(field)) is not None

def map_to_type(condition, field, whole_data, old_type, new_type):
    if any(condition(field, elem, whole_data) for elem in whole_data):
        return new_type
    return old_type

def map_fields(field, whole_data):
    variable_type = 'String'
    variable_name = field if '?' not in field else f'`{field}`'

    variable_type = map_to_type(is_object, field, whole_data, variable_type, f'Map<String, Any>')
    variable_type = map_to_type(is_number, field, whole_data, variable_type, f'Int')
    variable_type = map_to_type(is_float, field, whole_data, variable_type, f'Float')
    variable_type = map_to_type(is_array, field, whole_data, variable_type, f'Set<{variable_type}>')
    variable_type = map_to_type(is_array_of_object, field, whole_data, variable_type, f'Set<Map<String, Any>>')
    variable_type = map_to_type(is_nullable, field, whole_data, variable_type, f'{variable_type}? = null')

    return f'val `{variable_name}`: {variable_type}'


def is_nullable_field(x):
    return '?' in x


path = 'satisfactory-loader/src/main/kotlin/terapps/factoryplanner/bootstrap/dto'
classes= []
for x in data:
    nativeClass = x['NativeClass']

    className = nativeClass.replace("/Script/CoreUObject.Class'/Script/FactoryGame.", "").replace('\'', '')

    print(className)
    classes.append([nativeClass, className])
    fields = {}
    for obj in x['Classes']:
        for [k, v] in obj.items():
            fields[k] = ''
    field_keys = list(fields.keys())
    field_groups = itertools.groupby([map_fields(key, x['Classes']) for key in field_keys], is_nullable_field)
    nullable_fields = []
    required_fields = []
    for groupKey, g in field_groups:
        if groupKey:
            nullable_fields.extend(sorted(list(g)))
        else:
            required_fields.extend(sorted(list(g)))

    all_fields = required_fields + nullable_fields
    kotlinClass = """
    package terapps.factoryplanner.bootstrap.dto.generated
    import terapps.factoryplanner.bootstrap.dto.GameEntity

        data class {className}(
{fields}
): GameEntity()
    """.format(className=className, fields=',\n'.join(all_fields))

    f = open(f'{path}/generated/{className}.kt', 'w')
    f.write(kotlinClass)

mapped_classes = list(map(lambda x: "Type(value={className}::class, name=\"{fullPath}\")".format(
    className=x[1],
    fullPath=x[0]
), classes))

wrapper_class = """
package terapps.factoryplanner.bootstrap.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonSubTypes.Type
import terapps.factoryplanner.bootstrap.dto.generated.*

@JsonSubTypes(
        value = [
            {types}
        ]
)
open class GameEntity""".format(types=',\n'.join(mapped_classes))

f = open(f'{path}/GameEntity.kt', 'w')
f.write(wrapper_class)