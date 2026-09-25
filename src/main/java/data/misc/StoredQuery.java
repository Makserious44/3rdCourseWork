package data.misc;

import java.util.Map;

public record StoredQuery(String hql, Map<String, Object> parameters) { }
