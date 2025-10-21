package com.example.yvts

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt
import com.example.yvts.ui.theme.YvtsTheme
import androidx.compose.ui.unit.sp

data class SizeRow(val size: String, val chestCmRange: IntRange)

private val sizeTable = listOf(
    SizeRow("XXXS", 76..81),
    SizeRow("XXS", 81..86),
    SizeRow("XS", 86..91),
    SizeRow("S", 91..96),
    SizeRow("M", 96..102),
    SizeRow("L", 102..107),
    SizeRow("XL", 107..112),
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { YvtsTheme { SizeGuideScreen(onClose = { finish() }) } }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SizeGuideScreen(onClose: () -> Unit) {
    var unit by remember { mutableStateOf(MeasureUnit.Cm) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Size Guide") },
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }
            )
        }
    ) { inner ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner)
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            Text(
                "T-shirts & Polo Shirts",
                color = Color(0xFF5E5E5E),
                fontSize = 20.sp,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(20.dp))

            SegmentedTwoButtons(
                left = "cm",
                right = "inches",

                selectedRight = unit == MeasureUnit.Inch,
                onSelected = { rightSelected -> unit = if (rightSelected) MeasureUnit.Inch else MeasureUnit.Cm },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(Modifier.height(20.dp))

            SizeTable(unit = unit)
        }
    }
}

enum class MeasureUnit { Cm, Inch }

@Composable
private fun SizeTable(unit: MeasureUnit) {
    // Header
    Row(
        Modifier
            .fillMaxWidth()
            .background(Color(0xFFF6F6F6))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Size", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.weight(1f))
        Text(
            text = if (unit == MeasureUnit.Cm) "Chest" else "Chest",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.weight(1f),
        )
    }

    Divider(color = Color(0xFFEAEAEA))

    LazyColumn {
        items(sizeTable) { row ->
            SizeTableRow(
                left = row.size,
                right = when (unit) {
                    MeasureUnit.Cm -> "${row.chestCmRange.first}-${row.chestCmRange.last}"
                    MeasureUnit.Inch -> {
                        // см → инч ( /2.54 ) болгож байгаа ба хамгийн ойрын бүхэл тоо руу хөрвүүлсэн
                        val lo = (row.chestCmRange.first / 2.54).roundToInt()
                        val hi = (row.chestCmRange.last / 2.54).roundToInt()
                        "$lo-$hi"
                    }
                }
            )
            Divider(color = Color(0xFFEAEAEA))
        }
    }
}

@Composable
private fun SizeTableRow(left: String, right: String) {
    Row(
        Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(left, fontSize = 15.sp, modifier = Modifier.weight(1f))
        Text(
            right,
            fontSize = 15.sp,
            modifier = Modifier.weight(1f),
            color = Color.Black
        )
    }
}


@Composable
private fun SegmentedTwoButtons(
    left: String,
    right: String,
    selectedRight: Boolean,
    onSelected: (rightSelected: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val shapeLeft = RoundedCornerShape(topStart = 8.dp, bottomStart = 8.dp)
        val shapeRight = RoundedCornerShape(topEnd = 8.dp, bottomEnd = 8.dp)

        val common = Modifier.height(40.dp).widthIn(min = 90.dp)

        FilledTonalButton(
            onClick = { onSelected(false) },
            shape = shapeLeft,
            modifier = common,
            colors = if (!selectedRight) ButtonDefaults.filledTonalButtonColors()
            else ButtonDefaults.filledTonalButtonColors(containerColor = Color.Transparent)
        ) { Text(left) }

        FilledTonalButton(
            onClick = { onSelected(true) },
            shape = shapeRight,
            modifier = common,
            colors = if (selectedRight) ButtonDefaults.filledTonalButtonColors()
            else ButtonDefaults.filledTonalButtonColors(containerColor = Color.Transparent)
        ) { Text(right) }
    }
}
