(ns bluster.ing.into.nice.api.docs.generate)

;; Resource:  (:resourcePath :models :apiVersion :swaggerVersion :basePath :apis)
;; Model:     (:uniqueItems :properties :id :type :required)
;; API:       (:path :description :operations)
;; Operation: (:parameters :summary :httpMethod :errorResponses :nickname :responseClass)
;; Response Class: "ok" or Model.
;; Parameter: (:name :description :required :allowMultiple :paramType :dataType)
;; Error Response: (:reason :code) e.g. {:reason "Invalid ID supplied", :code 400}

;; Param Types: (path body query header)

;; Types:     (Date long string any)
;; Resource : [Model : [Property] ] [API : [Operation : [Parameter] [Error Response] ]

(def default-swagger-version "1.0")

(defn scaffold-parameter
  "paramType will default to query for GET, body for POST"
  [param-type parameter]
  (merge {:description ""
          :required true
          :allowMultiple false
          :dataType "string"
          :paramType param-type} parameter))

(defn scaffold-operation [operation]
  (let [parameters (:parameters operation)
        method     (:httpMethod operation)
        param-type (or (and (= method "POST") "body") "query")]
    (-> (merge {:summary        ""
                :errorResponses []
                :nickname       ""
                :responseClass  "ok"} operation)
        (assoc :parameters (mapv (partial scaffold-parameter param-type) parameters)))))

(defn scaffold-api [api]
  (let [operations (:operations api)]
    (assert (seq operations) "You have to provide operations or the API doesn't make any sense")
    (-> (merge {:description ""} api)
        (assoc :operations (mapv scaffold-operation operations)))))

(defn scaffold-property [[name property]]
  (let [initial (merge {:uniqueItems false
                        :type        "string"
                        :required    false
                        :description ""} property)
        type-of (:type property)
        format  (:format property)]
    [name (or (and (= type-of "integer") (not format) (assoc initial :format "int64")) initial)]))

(defn scaffold-model [[name model]]
  (let [properties (:properties model)
        new-props (mapv scaffold-property properties)]
    (assert (seq properties) "Must have properties list for the model or it's a no-op")
    [name
     (-> (merge {:uniqueItems []
                 :required false
                 :type "any"
                 :id name} model)
         (assoc :properties (into {} new-props)))]))

(defn scaffold-resource [resource]
  (assert (:resourcePath resource) "I can default the basePath to / but you have to provide a resource path.")
  (-> (merge {:basePath "/"
              :apiVersion "1.0"
              :swaggerVersion default-swagger-version} resource)
      (assoc :models (into {} (mapv scaffold-model (:models resource))))
      (assoc :apis   (mapv scaffold-api (:apis resource)))))
