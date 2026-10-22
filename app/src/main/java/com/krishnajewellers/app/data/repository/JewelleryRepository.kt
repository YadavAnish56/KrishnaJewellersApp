package com.krishnajewellers.app.data.repository

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.krishnajewellers.app.data.local.AppDatabaseHelper
import com.krishnajewellers.app.data.local.PreferencesManager
import com.krishnajewellers.app.data.model.CustomDesignRequest
import com.krishnajewellers.app.data.model.EnquiryConversation
import com.krishnajewellers.app.data.model.EnquiryMessage
import com.krishnajewellers.app.data.model.MakingChargeType
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.MetalType
import com.krishnajewellers.app.data.model.Product
import com.krishnajewellers.app.data.model.StoreInfo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class JewelleryRepository(
    private val dbHelper: AppDatabaseHelper,
    val preferences: PreferencesManager
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _rates = MutableStateFlow(MetalRates())
    val rates: StateFlow<MetalRates> = _rates.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _savedProductIds = MutableStateFlow<Set<String>>(emptySet())
    val savedProductIds: StateFlow<Set<String>> = _savedProductIds.asStateFlow()

    private val _likedProductIds = MutableStateFlow<Set<String>>(emptySet())
    val likedProductIds: StateFlow<Set<String>> = _likedProductIds.asStateFlow()

    private val _customRequests = MutableStateFlow<List<CustomDesignRequest>>(emptyList())
    val customRequests: StateFlow<List<CustomDesignRequest>> = _customRequests.asStateFlow()

    private val _conversations = MutableStateFlow<List<EnquiryConversation>>(emptyList())
    val conversations: StateFlow<List<EnquiryConversation>> = _conversations.asStateFlow()

    private val _messages = MutableStateFlow<Map<String, List<EnquiryMessage>>>(emptyMap())
    val messages: StateFlow<Map<String, List<EnquiryMessage>>> = _messages.asStateFlow()

    val storeInfo = StoreInfo()

    init {
        loadAllData()
    }

    private fun loadAllData() {
        scope.launch {
            loadRatesFromDb()
            loadProductsFromDb()
            loadFavouritesFromDb()
            loadCustomRequestsFromDb()
            loadConversationsFromDb()
            loadMessagesFromDb()
        }
    }

    // --- RATES ---
    private fun loadRatesFromDb() {
        val db = dbHelper.readableDatabase
        val cursor = db.query(AppDatabaseHelper.TABLE_RATES, null, null, null, null, null, null)
        cursor.use {
            if (it.moveToFirst()) {
                val gold24k = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COL_GOLD_24K))
                val gold22k = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COL_GOLD_22K))
                val gold18k = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COL_GOLD_18K))
                val silverKg = it.getDouble(it.getColumnIndexOrThrow(AppDatabaseHelper.COL_SILVER_KG))
                val updated = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COL_RATE_UPDATED))
                val updatedBy = it.getString(it.getColumnIndexOrThrow(AppDatabaseHelper.COL_RATE_UPDATED_BY))

                _rates.value = MetalRates(
                    id = 1,
                    gold24kPer10g = gold24k,
                    gold22kPer10g = gold22k,
                    gold18kPer10g = gold18k,
                    silverPerKg = silverKg,
                    lastUpdated = updated,
                    updatedBy = updatedBy
                )
            }
        }
    }

    fun updateRates(
        gold24k: Double,
        gold22k: Double,
        gold18k: Double,
        silverKg: Double,
        updatedBy: String = "Shop Owner Demo"
    ) {
        scope.launch {
            val timeFormat = SimpleDateFormat("hh:mm a", Locale.getDefault())
            val timeStr = "Today, " + timeFormat.format(Date())

            val db = dbHelper.writableDatabase
            val cv = ContentValues().apply {
                put(AppDatabaseHelper.COL_GOLD_24K, gold24k)
                put(AppDatabaseHelper.COL_GOLD_22K, gold22k)
                put(AppDatabaseHelper.COL_GOLD_18K, gold18k)
                put(AppDatabaseHelper.COL_SILVER_KG, silverKg)
                put(AppDatabaseHelper.COL_RATE_UPDATED, timeStr)
                put(AppDatabaseHelper.COL_RATE_UPDATED_BY, updatedBy)
            }
            db.update(AppDatabaseHelper.TABLE_RATES, cv, "${AppDatabaseHelper.COL_RATE_ID} = 1", null)

            _rates.value = MetalRates(
                id = 1,
                gold24kPer10g = gold24k,
                gold22kPer10g = gold22k,
                gold18kPer10g = gold18k,
                silverPerKg = silverKg,
                lastUpdated = timeStr,
                updatedBy = updatedBy
            )
        }
    }

    // --- PRODUCTS ---
    private fun loadProductsFromDb() {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<Product>()
        val cursor = db.query(AppDatabaseHelper.TABLE_PRODUCTS, null, null, null, null, null, null)
        cursor.use { c ->
            while (c.moveToNext()) {
                list.add(cursorToProduct(c))
            }
        }
        _products.value = list
    }

    private fun cursorToProduct(c: Cursor): Product {
        val galleryStr = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_GALLERY))
        val galleryList = if (galleryStr.isNullOrBlank()) emptyList() else galleryStr.split(",")

        return Product(
            id = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_ID)),
            name = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_NAME)),
            category = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_CATEGORY)),
            metalType = MetalType.valueOf(c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_METAL))),
            purity = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_PURITY)),
            description = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_DESC)),
            minWeight = c.getDouble(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_MIN_WEIGHT)),
            midWeight = c.getDouble(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_MID_WEIGHT)),
            maxWeight = c.getDouble(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_MAX_WEIGHT)),
            makingChargeType = MakingChargeType.valueOf(c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_CHARGE_TYPE))),
            makingChargeValue = c.getDouble(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_CHARGE_VAL)),
            imageResName = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_IMAGE)),
            galleryImageNames = galleryList,
            isTrending = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_TRENDING)) == 1,
            isMostLoved = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_MOST_LOVED)) == 1,
            isNewArrival = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_NEW)) == 1,
            isVisible = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_VISIBLE)) == 1,
            isDemoCreated = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_PROD_DEMO)) == 1
        )
    }

    fun addProduct(product: Product) {
        scope.launch {
            val db = dbHelper.writableDatabase
            val cv = ContentValues().apply {
                put(AppDatabaseHelper.COL_PROD_ID, product.id)
                put(AppDatabaseHelper.COL_PROD_NAME, product.name)
                put(AppDatabaseHelper.COL_PROD_CATEGORY, product.category)
                put(AppDatabaseHelper.COL_PROD_METAL, product.metalType.name)
                put(AppDatabaseHelper.COL_PROD_PURITY, product.purity)
                put(AppDatabaseHelper.COL_PROD_DESC, product.description)
                put(AppDatabaseHelper.COL_PROD_MIN_WEIGHT, product.minWeight)
                put(AppDatabaseHelper.COL_PROD_MID_WEIGHT, product.midWeight)
                put(AppDatabaseHelper.COL_PROD_MAX_WEIGHT, product.maxWeight)
                put(AppDatabaseHelper.COL_PROD_CHARGE_TYPE, product.makingChargeType.name)
                put(AppDatabaseHelper.COL_PROD_CHARGE_VAL, product.makingChargeValue)
                put(AppDatabaseHelper.COL_PROD_IMAGE, product.imageResName)
                put(AppDatabaseHelper.COL_PROD_GALLERY, product.galleryImageNames.joinToString(","))
                put(AppDatabaseHelper.COL_PROD_TRENDING, if (product.isTrending) 1 else 0)
                put(AppDatabaseHelper.COL_PROD_MOST_LOVED, if (product.isMostLoved) 1 else 0)
                put(AppDatabaseHelper.COL_PROD_NEW, if (product.isNewArrival) 1 else 0)
                put(AppDatabaseHelper.COL_PROD_VISIBLE, if (product.isVisible) 1 else 0)
                put(AppDatabaseHelper.COL_PROD_DEMO, if (product.isDemoCreated) 1 else 0)
            }
            db.insertWithOnConflict(
                AppDatabaseHelper.TABLE_PRODUCTS,
                null,
                cv,
                android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
            )
            loadProductsFromDb()
        }
    }

    fun updateProduct(product: Product) {
        addProduct(product)
    }

    fun deleteProduct(id: String) {
        scope.launch {
            val db = dbHelper.writableDatabase
            db.delete(AppDatabaseHelper.TABLE_PRODUCTS, "${AppDatabaseHelper.COL_PROD_ID} = ?", arrayOf(id))
            loadProductsFromDb()
        }
    }

    fun toggleProductVisibility(id: String, visible: Boolean) {
        scope.launch {
            val db = dbHelper.writableDatabase
            val cv = ContentValues().apply {
                put(AppDatabaseHelper.COL_PROD_VISIBLE, if (visible) 1 else 0)
            }
            db.update(AppDatabaseHelper.TABLE_PRODUCTS, cv, "${AppDatabaseHelper.COL_PROD_ID} = ?", arrayOf(id))
            loadProductsFromDb()
        }
    }

    fun getProductById(id: String): Product? {
        return _products.value.find { it.id == id }
    }

    // --- FAVOURITES / LIKES ---
    private fun loadFavouritesFromDb() {
        val db = dbHelper.readableDatabase
        val saved = mutableSetOf<String>()
        val liked = mutableSetOf<String>()
        val cursor = db.query(AppDatabaseHelper.TABLE_FAVOURITES, null, null, null, null, null, null)
        cursor.use { c ->
            while (c.moveToNext()) {
                val prodId = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_FAV_PROD_ID))
                val isLiked = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_FAV_LIKED)) == 1
                val isSaved = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_FAV_SAVED)) == 1
                if (isLiked) liked.add(prodId)
                if (isSaved) saved.add(prodId)
            }
        }
        _likedProductIds.value = liked
        _savedProductIds.value = saved
    }

    fun toggleSaved(productId: String) {
        scope.launch {
            val currentSaved = _savedProductIds.value.toMutableSet()
            val isNowSaved = !currentSaved.contains(productId)
            if (isNowSaved) {
                currentSaved.add(productId)
            } else {
                currentSaved.remove(productId)
            }
            _savedProductIds.value = currentSaved

            val isLiked = _likedProductIds.value.contains(productId)
            saveFavToDb(productId, isLiked, isNowSaved)
        }
    }

    fun toggleLiked(productId: String) {
        scope.launch {
            val currentLiked = _likedProductIds.value.toMutableSet()
            val isNowLiked = !currentLiked.contains(productId)
            if (isNowLiked) {
                currentLiked.add(productId)
            } else {
                currentLiked.remove(productId)
            }
            _likedProductIds.value = currentLiked

            val isSaved = _savedProductIds.value.contains(productId)
            saveFavToDb(productId, isNowLiked, isSaved)
        }
    }

    private fun saveFavToDb(productId: String, liked: Boolean, saved: Boolean) {
        val db = dbHelper.writableDatabase
        if (!liked && !saved) {
            db.delete(AppDatabaseHelper.TABLE_FAVOURITES, "${AppDatabaseHelper.COL_FAV_PROD_ID} = ?", arrayOf(productId))
        } else {
            val cv = ContentValues().apply {
                put(AppDatabaseHelper.COL_FAV_PROD_ID, productId)
                put(AppDatabaseHelper.COL_FAV_LIKED, if (liked) 1 else 0)
                put(AppDatabaseHelper.COL_FAV_SAVED, if (saved) 1 else 0)
                put(AppDatabaseHelper.COL_FAV_TIMESTAMP, System.currentTimeMillis())
            }
            db.insertWithOnConflict(
                AppDatabaseHelper.TABLE_FAVOURITES,
                null,
                cv,
                android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
            )
        }
    }

    // --- CUSTOM REQUESTS ---
    private fun loadCustomRequestsFromDb() {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<CustomDesignRequest>()
        val cursor = db.query(AppDatabaseHelper.TABLE_CUSTOM_REQUESTS, null, null, null, null, null, "${AppDatabaseHelper.COL_REQ_ID} DESC")
        cursor.use { c ->
            while (c.moveToNext()) {
                val attachStr = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_ATTACHMENTS))
                val attachments = if (attachStr.isNullOrBlank()) emptyList() else attachStr.split(",")

                val quotedCol = c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_QUOTE_PRICE)
                val quoted = if (c.isNull(quotedCol)) null else c.getDouble(quotedCol)

                list.add(
                    CustomDesignRequest(
                        id = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_ID)),
                        customerName = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_NAME)),
                        customerPhone = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_PHONE)),
                        title = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_TITLE)),
                        description = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_DESC)),
                        preferredWeight = c.getDouble(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_WEIGHT)),
                        budget = c.getDouble(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_BUDGET)),
                        occasion = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_OCCASION)),
                        attachments = attachments,
                        videoReference = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_VIDEO)),
                        status = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_STATUS)),
                        quotedPrice = quoted,
                        quoteNote = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_QUOTE_NOTE)),
                        date = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_REQ_DATE))
                    )
                )
            }
        }
        _customRequests.value = list
    }

    fun submitCustomRequest(req: CustomDesignRequest) {
        scope.launch {
            val db = dbHelper.writableDatabase
            val cv = ContentValues().apply {
                put(AppDatabaseHelper.COL_REQ_ID, req.id)
                put(AppDatabaseHelper.COL_REQ_NAME, req.customerName)
                put(AppDatabaseHelper.COL_REQ_PHONE, req.customerPhone)
                put(AppDatabaseHelper.COL_REQ_TITLE, req.title)
                put(AppDatabaseHelper.COL_REQ_DESC, req.description)
                put(AppDatabaseHelper.COL_REQ_WEIGHT, req.preferredWeight)
                put(AppDatabaseHelper.COL_REQ_BUDGET, req.budget)
                put(AppDatabaseHelper.COL_REQ_OCCASION, req.occasion)
                put(AppDatabaseHelper.COL_REQ_ATTACHMENTS, req.attachments.joinToString(","))
                put(AppDatabaseHelper.COL_REQ_VIDEO, req.videoReference)
                put(AppDatabaseHelper.COL_REQ_STATUS, req.status)
                put(AppDatabaseHelper.COL_REQ_QUOTE_PRICE, req.quotedPrice)
                put(AppDatabaseHelper.COL_REQ_QUOTE_NOTE, req.quoteNote)
                put(AppDatabaseHelper.COL_REQ_DATE, req.date)
            }
            db.insert(AppDatabaseHelper.TABLE_CUSTOM_REQUESTS, null, cv)
            loadCustomRequestsFromDb()
        }
    }

    fun updateCustomRequestQuote(id: String, status: String, price: Double, note: String) {
        scope.launch {
            val db = dbHelper.writableDatabase
            val cv = ContentValues().apply {
                put(AppDatabaseHelper.COL_REQ_STATUS, status)
                put(AppDatabaseHelper.COL_REQ_QUOTE_PRICE, price)
                put(AppDatabaseHelper.COL_REQ_QUOTE_NOTE, note)
            }
            db.update(AppDatabaseHelper.TABLE_CUSTOM_REQUESTS, cv, "${AppDatabaseHelper.COL_REQ_ID} = ?", arrayOf(id))
            loadCustomRequestsFromDb()
        }
    }

    // --- CONVERSATIONS & CHAT ---
    private fun loadConversationsFromDb() {
        val db = dbHelper.readableDatabase
        val list = mutableListOf<EnquiryConversation>()
        val cursor = db.query(AppDatabaseHelper.TABLE_CONVERSATIONS, null, null, null, null, null, "${AppDatabaseHelper.COL_CONV_LAST_TIME} DESC")
        cursor.use { c ->
            while (c.moveToNext()) {
                list.add(
                    EnquiryConversation(
                        id = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_ID)),
                        customerName = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_NAME)),
                        customerPhone = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_PHONE)),
                        lastMessage = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_LAST_MSG)),
                        lastTimestamp = c.getLong(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_LAST_TIME)),
                        unreadCount = c.getInt(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_UNREAD)),
                        productId = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_PROD_ID)),
                        productName = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_PROD_NAME)),
                        productImage = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_CONV_PROD_IMG))
                    )
                )
            }
        }
        _conversations.value = list
    }

    private fun loadMessagesFromDb() {
        val db = dbHelper.readableDatabase
        val map = mutableMapOf<String, MutableList<EnquiryMessage>>()
        val cursor = db.query(AppDatabaseHelper.TABLE_MESSAGES, null, null, null, null, null, "${AppDatabaseHelper.COL_MSG_TIME} ASC")
        cursor.use { c ->
            while (c.moveToNext()) {
                val convId = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_CONV_ID))
                val priceCol = c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_PROD_PRICE)
                val price = if (c.isNull(priceCol)) null else c.getDouble(priceCol)

                val msg = EnquiryMessage(
                    id = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_ID)),
                    conversationId = convId,
                    sender = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_SENDER)),
                    message = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_TEXT)),
                    timestamp = c.getLong(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_TIME)),
                    attachedProductId = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_PROD_ID)),
                    attachedProductName = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_PROD_NAME)),
                    attachedProductCategory = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_PROD_CAT)),
                    attachedProductImage = c.getString(c.getColumnIndexOrThrow(AppDatabaseHelper.COL_MSG_PROD_IMG)),
                    attachedProductPrice = price
                )
                map.getOrPut(convId) { mutableListOf() }.add(msg)
            }
        }
        _messages.value = map
    }

    fun sendMessage(
        conversationId: String,
        sender: String,
        text: String,
        product: Product? = null,
        calculatedPrice: Double? = null
    ) {
        scope.launch {
            val now = System.currentTimeMillis()
            val msgId = "msg-" + UUID.randomUUID().toString().take(8)

            val msg = EnquiryMessage(
                id = msgId,
                conversationId = conversationId,
                sender = sender,
                message = text,
                timestamp = now,
                attachedProductId = product?.id,
                attachedProductName = product?.name,
                attachedProductCategory = product?.category,
                attachedProductImage = product?.imageResName,
                attachedProductPrice = calculatedPrice
            )

            val db = dbHelper.writableDatabase
            val msgCv = ContentValues().apply {
                put(AppDatabaseHelper.COL_MSG_ID, msg.id)
                put(AppDatabaseHelper.COL_MSG_CONV_ID, msg.conversationId)
                put(AppDatabaseHelper.COL_MSG_SENDER, msg.sender)
                put(AppDatabaseHelper.COL_MSG_TEXT, msg.message)
                put(AppDatabaseHelper.COL_MSG_TIME, msg.timestamp)
                put(AppDatabaseHelper.COL_MSG_PROD_ID, msg.attachedProductId)
                put(AppDatabaseHelper.COL_MSG_PROD_NAME, msg.attachedProductName)
                put(AppDatabaseHelper.COL_MSG_PROD_CAT, msg.attachedProductCategory)
                put(AppDatabaseHelper.COL_MSG_PROD_IMG, msg.attachedProductImage)
                put(AppDatabaseHelper.COL_MSG_PROD_PRICE, msg.attachedProductPrice)
            }
            db.insert(AppDatabaseHelper.TABLE_MESSAGES, null, msgCv)

            // Update conversation last message
            val convCv = ContentValues().apply {
                put(AppDatabaseHelper.COL_CONV_LAST_MSG, text)
                put(AppDatabaseHelper.COL_CONV_LAST_TIME, now)
                if (sender == "customer") {
                    put(AppDatabaseHelper.COL_CONV_UNREAD, 1)
                } else {
                    put(AppDatabaseHelper.COL_CONV_UNREAD, 0)
                }
            }
            db.update(AppDatabaseHelper.TABLE_CONVERSATIONS, convCv, "${AppDatabaseHelper.COL_CONV_ID} = ?", arrayOf(conversationId))

            loadConversationsFromDb()
            loadMessagesFromDb()
        }
    }

    fun startProductEnquiry(
        product: Product,
        customerName: String = "Valued Customer",
        customerPhone: String = "+91 98251 98765",
        customMessage: String? = null
    ): String {
        val existing = _conversations.value.find { it.productId == product.id }
        if (existing != null) {
            val text = customMessage ?: "Namaste Krishna Jewellers, I would like to enquire about ${product.name} (${product.purity})."
            sendMessage(existing.id, "customer", text, product, product.calculatePrice(_rates.value).totalEstimatedPrice.toDouble())
            return existing.id
        }

        val convId = "conv-" + UUID.randomUUID().toString().take(8)
        val now = System.currentTimeMillis()
        val text = customMessage ?: "Namaste Krishna Jewellers, I would like to enquire about ${product.name} (${product.purity}). Please share showroom availability and viewing timings."

        scope.launch {
            val db = dbHelper.writableDatabase
            val convCv = ContentValues().apply {
                put(AppDatabaseHelper.COL_CONV_ID, convId)
                put(AppDatabaseHelper.COL_CONV_NAME, customerName)
                put(AppDatabaseHelper.COL_CONV_PHONE, customerPhone)
                put(AppDatabaseHelper.COL_CONV_LAST_MSG, text)
                put(AppDatabaseHelper.COL_CONV_LAST_TIME, now)
                put(AppDatabaseHelper.COL_CONV_UNREAD, 1)
                put(AppDatabaseHelper.COL_CONV_PROD_ID, product.id)
                put(AppDatabaseHelper.COL_CONV_PROD_NAME, product.name)
                put(AppDatabaseHelper.COL_CONV_PROD_IMG, product.imageResName)
            }
            db.insert(AppDatabaseHelper.TABLE_CONVERSATIONS, null, convCv)

            val price = product.calculatePrice(_rates.value).totalEstimatedPrice.toDouble()
            val msgId = "msg-" + UUID.randomUUID().toString().take(8)
            val msgCv = ContentValues().apply {
                put(AppDatabaseHelper.COL_MSG_ID, msgId)
                put(AppDatabaseHelper.COL_MSG_CONV_ID, convId)
                put(AppDatabaseHelper.COL_MSG_SENDER, "customer")
                put(AppDatabaseHelper.COL_MSG_TEXT, text)
                put(AppDatabaseHelper.COL_MSG_TIME, now)
                put(AppDatabaseHelper.COL_MSG_PROD_ID, product.id)
                put(AppDatabaseHelper.COL_MSG_PROD_NAME, product.name)
                put(AppDatabaseHelper.COL_MSG_PROD_CAT, product.category)
                put(AppDatabaseHelper.COL_MSG_PROD_IMG, product.imageResName)
                put(AppDatabaseHelper.COL_MSG_PROD_PRICE, price)
            }
            db.insert(AppDatabaseHelper.TABLE_MESSAGES, null, msgCv)

            loadConversationsFromDb()
            loadMessagesFromDb()
        }
        return convId
    }

    fun markConversationRead(conversationId: String) {
        scope.launch {
            val db = dbHelper.writableDatabase
            val cv = ContentValues().apply {
                put(AppDatabaseHelper.COL_CONV_UNREAD, 0)
            }
            db.update(AppDatabaseHelper.TABLE_CONVERSATIONS, cv, "${AppDatabaseHelper.COL_CONV_ID} = ?", arrayOf(conversationId))
            loadConversationsFromDb()
        }
    }
}
