package com.krishnajewellers.app.data.local

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.krishnajewellers.app.data.model.CustomDesignRequest
import com.krishnajewellers.app.data.model.EnquiryConversation
import com.krishnajewellers.app.data.model.EnquiryMessage
import com.krishnajewellers.app.data.model.MakingChargeType
import com.krishnajewellers.app.data.model.MetalRates
import com.krishnajewellers.app.data.model.MetalType
import com.krishnajewellers.app.data.model.Product

class AppDatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        const val DATABASE_NAME = "krishna_jewellers.db"
        const val DATABASE_VERSION = 2

        // Table: Metal Rates
        const val TABLE_RATES = "metal_rates"
        const val COL_RATE_ID = "id"
        const val COL_GOLD_24K = "gold_24k"
        const val COL_GOLD_22K = "gold_22k"
        const val COL_GOLD_18K = "gold_18k"
        const val COL_SILVER_KG = "silver_kg"
        const val COL_RATE_UPDATED = "last_updated"
        const val COL_RATE_UPDATED_BY = "updated_by"

        // Table: Products
        const val TABLE_PRODUCTS = "products"
        const val COL_PROD_ID = "id"
        const val COL_PROD_NAME = "name"
        const val COL_PROD_CATEGORY = "category"
        const val COL_PROD_METAL = "metal_type"
        const val COL_PROD_PURITY = "purity"
        const val COL_PROD_DESC = "description"
        const val COL_PROD_MIN_WEIGHT = "min_weight"
        const val COL_PROD_MID_WEIGHT = "mid_weight"
        const val COL_PROD_MAX_WEIGHT = "max_weight"
        const val COL_PROD_CHARGE_TYPE = "making_charge_type"
        const val COL_PROD_CHARGE_VAL = "making_charge_value"
        const val COL_PROD_IMAGE = "image_res"
        const val COL_PROD_GALLERY = "gallery_res"
        const val COL_PROD_TRENDING = "is_trending"
        const val COL_PROD_MOST_LOVED = "is_most_loved"
        const val COL_PROD_NEW = "is_new"
        const val COL_PROD_VISIBLE = "is_visible"
        const val COL_PROD_DEMO = "is_demo_created"

        // Table: Saved / Liked
        const val TABLE_FAVOURITES = "favourites"
        const val COL_FAV_PROD_ID = "product_id"
        const val COL_FAV_LIKED = "is_liked"
        const val COL_FAV_SAVED = "is_saved"
        const val COL_FAV_TIMESTAMP = "timestamp"

        // Table: Custom Requests
        const val TABLE_CUSTOM_REQUESTS = "custom_requests"
        const val COL_REQ_ID = "id"
        const val COL_REQ_NAME = "customer_name"
        const val COL_REQ_PHONE = "customer_phone"
        const val COL_REQ_TITLE = "title"
        const val COL_REQ_DESC = "description"
        const val COL_REQ_WEIGHT = "preferred_weight"
        const val COL_REQ_BUDGET = "budget"
        const val COL_REQ_OCCASION = "occasion"
        const val COL_REQ_ATTACHMENTS = "attachments"
        const val COL_REQ_VIDEO = "video_reference"
        const val COL_REQ_STATUS = "status"
        const val COL_REQ_QUOTE_PRICE = "quoted_price"
        const val COL_REQ_QUOTE_NOTE = "quote_note"
        const val COL_REQ_DATE = "date"

        // Table: Enquiry Conversations
        const val TABLE_CONVERSATIONS = "conversations"
        const val COL_CONV_ID = "id"
        const val COL_CONV_NAME = "customer_name"
        const val COL_CONV_PHONE = "customer_phone"
        const val COL_CONV_LAST_MSG = "last_message"
        const val COL_CONV_LAST_TIME = "last_timestamp"
        const val COL_CONV_UNREAD = "unread_count"
        const val COL_CONV_PROD_ID = "product_id"
        const val COL_CONV_PROD_NAME = "product_name"
        const val COL_CONV_PROD_IMG = "product_image"

        // Table: Enquiry Messages
        const val TABLE_MESSAGES = "messages"
        const val COL_MSG_ID = "id"
        const val COL_MSG_CONV_ID = "conversation_id"
        const val COL_MSG_SENDER = "sender"
        const val COL_MSG_TEXT = "message"
        const val COL_MSG_TIME = "timestamp"
        const val COL_MSG_PROD_ID = "attached_product_id"
        const val COL_MSG_PROD_NAME = "attached_product_name"
        const val COL_MSG_PROD_CAT = "attached_product_cat"
        const val COL_MSG_PROD_IMG = "attached_product_img"
        const val COL_MSG_PROD_PRICE = "attached_product_price"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(
            """
            CREATE TABLE $TABLE_RATES (
                $COL_RATE_ID INTEGER PRIMARY KEY,
                $COL_GOLD_24K REAL NOT NULL,
                $COL_GOLD_22K REAL NOT NULL,
                $COL_GOLD_18K REAL NOT NULL,
                $COL_SILVER_KG REAL NOT NULL,
                $COL_RATE_UPDATED TEXT NOT NULL,
                $COL_RATE_UPDATED_BY TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_PRODUCTS (
                $COL_PROD_ID TEXT PRIMARY KEY,
                $COL_PROD_NAME TEXT NOT NULL,
                $COL_PROD_CATEGORY TEXT NOT NULL,
                $COL_PROD_METAL TEXT NOT NULL,
                $COL_PROD_PURITY TEXT NOT NULL,
                $COL_PROD_DESC TEXT NOT NULL,
                $COL_PROD_MIN_WEIGHT REAL NOT NULL,
                $COL_PROD_MID_WEIGHT REAL NOT NULL,
                $COL_PROD_MAX_WEIGHT REAL NOT NULL,
                $COL_PROD_CHARGE_TYPE TEXT NOT NULL,
                $COL_PROD_CHARGE_VAL REAL NOT NULL,
                $COL_PROD_IMAGE TEXT NOT NULL,
                $COL_PROD_GALLERY TEXT NOT NULL,
                $COL_PROD_TRENDING INTEGER NOT NULL,
                $COL_PROD_MOST_LOVED INTEGER NOT NULL,
                $COL_PROD_NEW INTEGER NOT NULL,
                $COL_PROD_VISIBLE INTEGER NOT NULL,
                $COL_PROD_DEMO INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_FAVOURITES (
                $COL_FAV_PROD_ID TEXT PRIMARY KEY,
                $COL_FAV_LIKED INTEGER NOT NULL,
                $COL_FAV_SAVED INTEGER NOT NULL,
                $COL_FAV_TIMESTAMP INTEGER NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_CUSTOM_REQUESTS (
                $COL_REQ_ID TEXT PRIMARY KEY,
                $COL_REQ_NAME TEXT NOT NULL,
                $COL_REQ_PHONE TEXT NOT NULL,
                $COL_REQ_TITLE TEXT NOT NULL,
                $COL_REQ_DESC TEXT NOT NULL,
                $COL_REQ_WEIGHT REAL NOT NULL,
                $COL_REQ_BUDGET REAL NOT NULL,
                $COL_REQ_OCCASION TEXT NOT NULL,
                $COL_REQ_ATTACHMENTS TEXT NOT NULL,
                $COL_REQ_VIDEO TEXT,
                $COL_REQ_STATUS TEXT NOT NULL,
                $COL_REQ_QUOTE_PRICE REAL,
                $COL_REQ_QUOTE_NOTE TEXT,
                $COL_REQ_DATE TEXT NOT NULL
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_CONVERSATIONS (
                $COL_CONV_ID TEXT PRIMARY KEY,
                $COL_CONV_NAME TEXT NOT NULL,
                $COL_CONV_PHONE TEXT NOT NULL,
                $COL_CONV_LAST_MSG TEXT NOT NULL,
                $COL_CONV_LAST_TIME INTEGER NOT NULL,
                $COL_CONV_UNREAD INTEGER NOT NULL,
                $COL_CONV_PROD_ID TEXT,
                $COL_CONV_PROD_NAME TEXT,
                $COL_CONV_PROD_IMG TEXT
            )
            """.trimIndent()
        )

        db.execSQL(
            """
            CREATE TABLE $TABLE_MESSAGES (
                $COL_MSG_ID TEXT PRIMARY KEY,
                $COL_MSG_CONV_ID TEXT NOT NULL,
                $COL_MSG_SENDER TEXT NOT NULL,
                $COL_MSG_TEXT TEXT NOT NULL,
                $COL_MSG_TIME INTEGER NOT NULL,
                $COL_MSG_PROD_ID TEXT,
                $COL_MSG_PROD_NAME TEXT,
                $COL_MSG_PROD_CAT TEXT,
                $COL_MSG_PROD_IMG TEXT,
                $COL_MSG_PROD_PRICE REAL
            )
            """.trimIndent()
        )

        // Seed initial precious metal rates
        val ratesCv = ContentValues().apply {
            put(COL_RATE_ID, 1)
            put(COL_GOLD_24K, 73500.0)
            put(COL_GOLD_22K, 67375.0)
            put(COL_GOLD_18K, 55125.0)
            put(COL_SILVER_KG, 89000.0)
            put(COL_RATE_UPDATED, "Today, 10:30 AM")
            put(COL_RATE_UPDATED_BY, "Surat Showroom Admin")
        }
        db.insert(TABLE_RATES, null, ratesCv)

        // Seed demo products
        SeedData.initialProducts.forEach { p ->
            insertProductRow(db, p)
        }

        // Seed initial welcome conversation
        SeedData.initialConversation?.let { conv ->
            insertConversationRow(db, conv)
        }
        SeedData.initialMessages.forEach { msg ->
            insertMessageRow(db, msg)
        }

        // Seed sample custom design request
        SeedData.initialCustomRequests.forEach { req ->
            insertCustomRequestRow(db, req)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_RATES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_PRODUCTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_FAVOURITES")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CUSTOM_REQUESTS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_CONVERSATIONS")
        db.execSQL("DROP TABLE IF EXISTS $TABLE_MESSAGES")
        onCreate(db)
    }

    private fun insertProductRow(db: SQLiteDatabase, p: Product) {
        val cv = ContentValues().apply {
            put(COL_PROD_ID, p.id)
            put(COL_PROD_NAME, p.name)
            put(COL_PROD_CATEGORY, p.category)
            put(COL_PROD_METAL, p.metalType.name)
            put(COL_PROD_PURITY, p.purity)
            put(COL_PROD_DESC, p.description)
            put(COL_PROD_MIN_WEIGHT, p.minWeight)
            put(COL_PROD_MID_WEIGHT, p.midWeight)
            put(COL_PROD_MAX_WEIGHT, p.maxWeight)
            put(COL_PROD_CHARGE_TYPE, p.makingChargeType.name)
            put(COL_PROD_CHARGE_VAL, p.makingChargeValue)
            put(COL_PROD_IMAGE, p.imageResName)
            put(COL_PROD_GALLERY, p.galleryImageNames.joinToString(","))
            put(COL_PROD_TRENDING, if (p.isTrending) 1 else 0)
            put(COL_PROD_MOST_LOVED, if (p.isMostLoved) 1 else 0)
            put(COL_PROD_NEW, if (p.isNewArrival) 1 else 0)
            put(COL_PROD_VISIBLE, if (p.isVisible) 1 else 0)
            put(COL_PROD_DEMO, if (p.isDemoCreated) 1 else 0)
        }
        db.insert(TABLE_PRODUCTS, null, cv)
    }

    private fun insertConversationRow(db: SQLiteDatabase, conv: EnquiryConversation) {
        val cv = ContentValues().apply {
            put(COL_CONV_ID, conv.id)
            put(COL_CONV_NAME, conv.customerName)
            put(COL_CONV_PHONE, conv.customerPhone)
            put(COL_CONV_LAST_MSG, conv.lastMessage)
            put(COL_CONV_LAST_TIME, conv.lastTimestamp)
            put(COL_CONV_UNREAD, conv.unreadCount)
            put(COL_CONV_PROD_ID, conv.productId)
            put(COL_CONV_PROD_NAME, conv.productName)
            put(COL_CONV_PROD_IMG, conv.productImage)
        }
        db.insert(TABLE_CONVERSATIONS, null, cv)
    }

    private fun insertMessageRow(db: SQLiteDatabase, msg: EnquiryMessage) {
        val cv = ContentValues().apply {
            put(COL_MSG_ID, msg.id)
            put(COL_MSG_CONV_ID, msg.conversationId)
            put(COL_MSG_SENDER, msg.sender)
            put(COL_MSG_TEXT, msg.message)
            put(COL_MSG_TIME, msg.timestamp)
            put(COL_MSG_PROD_ID, msg.attachedProductId)
            put(COL_MSG_PROD_NAME, msg.attachedProductName)
            put(COL_MSG_PROD_CAT, msg.attachedProductCategory)
            put(COL_MSG_PROD_IMG, msg.attachedProductImage)
            put(COL_MSG_PROD_PRICE, msg.attachedProductPrice)
        }
        db.insert(TABLE_MESSAGES, null, cv)
    }

    private fun insertCustomRequestRow(db: SQLiteDatabase, req: CustomDesignRequest) {
        val cv = ContentValues().apply {
            put(COL_REQ_ID, req.id)
            put(COL_REQ_NAME, req.customerName)
            put(COL_REQ_PHONE, req.customerPhone)
            put(COL_REQ_TITLE, req.title)
            put(COL_REQ_DESC, req.description)
            put(COL_REQ_WEIGHT, req.preferredWeight)
            put(COL_REQ_BUDGET, req.budget)
            put(COL_REQ_OCCASION, req.occasion)
            put(COL_REQ_ATTACHMENTS, req.attachments.joinToString(","))
            put(COL_REQ_VIDEO, req.videoReference)
            put(COL_REQ_STATUS, req.status)
            put(COL_REQ_QUOTE_PRICE, req.quotedPrice)
            put(COL_REQ_QUOTE_NOTE, req.quoteNote)
            put(COL_REQ_DATE, req.date)
        }
        db.insert(TABLE_CUSTOM_REQUESTS, null, cv)
    }
}
