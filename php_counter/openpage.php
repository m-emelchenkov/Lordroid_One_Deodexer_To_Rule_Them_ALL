<?php

$hit_count = @file_get_contents('openPageCount.txt');
$hit_count++;
@file_put_contents('openPageCount.txt', $hit_count);

header('Location: https://github.com/lord-ralf-adolf/Lordroid_One_Deodexer_To_Rule_Them_ALL/releases/latest'); // redirect to the real file to be downloaded
