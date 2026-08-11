<?php

return [
    Symfony\Bundle\FrameworkBundle\FrameworkBundle::class => ['all' => true],
<#if database?? && (database == "POSTGRES" || database == "MYSQL")>
    Doctrine\Bundle\DoctrineBundle\DoctrineBundle::class => ['all' => true],
    Doctrine\Bundle\MigrationsBundle\DoctrineMigrationsBundle::class => ['all' => true],
</#if>
<#if dependencies??>
<#if dependencies?seq_contains("security")>
    Symfony\Bundle\SecurityBundle\SecurityBundle::class => ['all' => true],
</#if>
<#if dependencies?seq_contains("twig")>
    Symfony\Bundle\TwigBundle\TwigBundle::class => ['all' => true],
</#if>
<#if dependencies?seq_contains("maker")>
    Symfony\Bundle\MakerBundle\MakerBundle::class => ['dev' => true],
</#if>
</#if>
];
