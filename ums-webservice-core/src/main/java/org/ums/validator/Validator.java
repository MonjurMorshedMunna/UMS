package org.ums.validator;


import javax.json.JsonObject;

public interface Validator {
  void validate(final JsonObject pJsonObject) throws ValidationException;
}
