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

(def user-resource
  {:resourcePath "/user"
   :basePath     "/api/v1/woohoo"
   :apiVersion   "1.0"
   :models       {:User user-model}
   :apis         [user-api]
   })

(def generated-resource
  {:apis
   [{:operations
     [{:parameters
       [{:name "limit",
         :description "",
         :required true,
         :allowMultiple false,
         :dataType "integer",
         :paramType "query"}
        {:name "offset",
         :description "",
         :required true,
         :allowMultiple false,
         :dataType "integer",
         :paramType "query"}
        {:name "query",
         :description "",
         :required true,
         :allowMultiple false,
         :dataType "string",
         :paramType "query"}],
       :httpMethod "GET",
       :summary "queries for users",
       :errorResponses [{:reason "No users matching query", :code 404}],
       :nickname "",
       :responseClass "Array[User]"}],
     :path "/query",
     :description "Paginated querying of users"}],
   :models
   {:User
    {:properties
     {:userId
      {:format "int64",
       :uniqueItems false,
       :type "integer",
       :required false,
       :description ""},
      :username
      {:uniqueItems false,
       :type "string",
       :required false,
       :description ""},
      :joined

      {:uniqueItems false,
       :type "Date",
       :required false,
       :description ""}},
     :uniqueItems [],
     :required false,
     :type "any",
     :id :User}},
   :resourcePath "/user",
   :basePath "/api/v1/woohoo",
   :apiVersion "1.0",
   :swaggerVersion "1.0"})

(deftest scaffolding
  (testing "Bad specs fail"
    (is (thrown? AssertionError (scaffold-resource bad-resource))))

  (testing "Good specs succeed"
    (is (= generated-resource (scaffold-resource user-resource)))))
