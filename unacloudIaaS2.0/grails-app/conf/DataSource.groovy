dataSource {
    pooled = true
	// Other database parameters..
	properties {
	   maxActive = 50
	   maxIdle = 25
	   minIdle = 5
	   initialSize = 5
	   minEvictableIdleTimeMillis = 1800000
	   timeBetweenEvictionRunsMillis = 1800000
	   maxWait = 10000
	}
    driverClassName = "com.mysql.jdbc.Driver"
	dialect = "org.hibernate.dialect.MySQL5InnoDBDialect"
	
}
hibernate {
	show_sql=false
    cache.use_second_level_cache = true
    cache.use_query_cache = false
    cache.region.factory_class = 'org.hibernate.cache.ehcache.EhCacheRegionFactory'
}
// environment specific settings
environments {
    development {
        dataSource {
			username = "root"
			password = "root"
            dbCreate = "update" // one of 'create', 'create-drop', 'update', 'validate', ''
            url = "jdbc:mysql://127.0.0.1:100/iaas2dev?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true"
        }
    }
    test {
        dataSource {
			username = "iaasprod"
			password = "6659a954f47c63b4bbd296c0efe0b73a"
            dbCreate = "update"
            url = "jdbc:mysql://157.253.236.160:3306/iaas2test?useUnicode=yes&characterEncoding=UTF-8&autoReconnect=true"
        }
    }
    production {
        dataSource {
			username = "iaasprod"
			password = "6659a954f47c63b4bbd296c0efe0b73a"
            dbCreate = "update"
            url = "jdbc:mysql://157.253.236.160:3306/iaas2prod?useUnicode=yes&characterEncoding=UTF-8"
            pooled = true
            properties {
               maxActive = -1
               minEvictableIdleTimeMillis=1800000
               timeBetweenEvictionRunsMillis=1800000
               numTestsPerEvictionRun=3
               testOnBorrow=true
                testWhileIdle=true
               testOnReturn=true
               validationQuery="SELECT 1"
            }
        }
    }
}
