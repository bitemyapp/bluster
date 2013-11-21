(ns bluster.generate-test
  (:require [clojure.test :refer :all]
            [cheshire.core :refer :all]
            [clj-http.client :as client]
            [bluster.ing.into.nice.api.docs.generate :refer :all]))

(def bad-resource {})

(def user-model
  {:properties {:userId   {:type "integer"}
                :username {:type "string"}
                :joined   {:type "Date"}}})

(def user-api
  {:path "/query"
   :description "Paginated querying of users"
   :operations [{:parameters [{:name "limit"
                               :paramType "query"
                               :dataType "integer"}
                              {:name "offset"
                               :paramType "query"
                               :dataType "integer"}
                              {:name "query"
                               :paramType "query"
                               :dataType "string"}
                              ]
                 :summary "queries for users"
                 :httpMethod "GET"
                 :responseClass "Array[User]"
                 :errorResponses
                 [{:reason "No users matching query" :code 404}]}]})

(def good-resource
  {:resourcePath "/user"
   :basePath     "/api/v1/woohoo"
   :apiVersion   "1.0"
   :models       {:User user-model}
   :apis         [user-api]
   })

(def generated-resource
  {:apis
   [{:path "/query",
     :operations
     [{:errorResponses [{:reason "No users matching query", :code 404}],
       :responseClass "Array[User]",
       :httpMethod "GET",
       :parameters
       [{:allowMultiple false,
         :name "limit",
         :paramType "query",
         :dataType "integer",
         :required true,
         :description ""}
        {:allowMultiple false,
         :name "offset",
         :paramType "query",
         :dataType "integer",
         :required true,
         :description ""}
        {:allowMultiple false,
         :name "query",
         :paramType "query",
         :dataType "string",
         :required true,
         :description ""}],
       :nickname "",
       :summary "queries for users"}],
     :description "Paginated querying of users"}],
   :models
   {:properties
    {:uniqueItems false,
     :type "Date",
     :required false,
     :format "int64",
     :description ""},
    :uniqueItems [],
    :required false,
    :type "any",
    :id :User},
   :resourcePath "/user",
   :basePath "/api/v1/woohoo",
   :apiVersion "1.0",
   :swaggerVersion
   "1.0"})


(deftest scaffolding
  (testing "Bad specs fail"
    (is (thrown? AssertionError (scaffold-resource bad-resource))))

  (testing "Good specs succeed"
    (is (= generated-resource (scaffold-resource good-resource)))))
